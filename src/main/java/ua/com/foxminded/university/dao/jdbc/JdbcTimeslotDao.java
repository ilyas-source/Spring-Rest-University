package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.TimeslotDao;
import ua.com.foxminded.university.dao.jdbc.mappers.TimeslotMapper;
import ua.com.foxminded.university.model.Timeslot;

@Component
public class JdbcTimeslotDao implements TimeslotDao {

    private static final String CREATE = "INSERT INTO timeslots (begin_time, end_time) VALUES (?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM timeslots WHERE id = ?";
    private static final String FIND_BY_BOTH_TIMES = "SELECT * FROM timeslots WHERE begin_time = ? AND end_time = ?";
    private static final String FIND_ALL = "SELECT * FROM timeslots";
    private static final String UPDATE = "UPDATE timeslots SET begin_time = ?, end_time = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM timeslots WHERE id = ?";
    private static final String COUNT_INTERSECTING_TIMESLOTS = "SELECT count(*) FROM timeslots WHERE end_time >= ? AND begin_time <= ?";

    private JdbcTemplate jdbcTemplate;
    private TimeslotMapper timeslotMapper;

    public JdbcTimeslotDao(JdbcTemplate jdbcTemplate, TimeslotMapper timeslotMapper) {
	this.jdbcTemplate = jdbcTemplate;
	this.timeslotMapper = timeslotMapper;
    }

    @Override
    public int countIntersectingTimeslots(Timeslot timeslot) {
	return jdbcTemplate.queryForObject(COUNT_INTERSECTING_TIMESLOTS, Integer.class, timeslot.getBeginTime(),
		timeslot.getEndTime());
    }

    @Override
    public void create(Timeslot timeslot) {
	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcTemplate.update(connection -> {
	    PreparedStatement ps = connection
		    .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
	    ps.setObject(1, timeslot.getBeginTime());
	    ps.setObject(2, timeslot.getEndTime());
	    return ps;
	}, keyHolder);
	timeslot.setId((int) keyHolder.getKeys().get("id"));
    }

    @Override
    public Optional<Timeslot> findByBothTimes(Timeslot timeslot) {
	try {
	    return Optional.of(jdbcTemplate.queryForObject(FIND_BY_BOTH_TIMES, timeslotMapper, timeslot.getBeginTime(),
		    timeslot.getEndTime()));
	} catch (EmptyResultDataAccessException e) {
	    return Optional.empty();
	}
    }

    @Override
    public Optional<Timeslot> findById(int id) {
	try {
	    return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, timeslotMapper, id));
	} catch (EmptyResultDataAccessException e) {
	    return Optional.empty();
	}
    }

    @Override
    public List<Timeslot> findAll() {
	return jdbcTemplate.query(FIND_ALL, timeslotMapper);
    }

    @Override
    public void update(Timeslot timeslot) {
	jdbcTemplate.update(UPDATE, timeslot.getBeginTime(), timeslot.getEndTime(), timeslot.getId());
    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
