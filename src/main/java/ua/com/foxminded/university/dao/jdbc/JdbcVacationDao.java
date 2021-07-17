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

import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.dao.jdbc.mappers.VacationMapper;
import ua.com.foxminded.university.model.Vacation;

@Component
public class JdbcVacationDao implements VacationDao {

    private static final String CREATE = "INSERT INTO vacations (start_date, end_date) VALUES (?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM vacations WHERE id = ?";
    private static final String FIND_BY_BOTH_DATES = "SELECT * FROM vacations WHERE start_date = ? AND end_date = ?";
    private static final String FIND_ALL = "SELECT * FROM vacations";
    private static final String UPDATE = "UPDATE vacations SET start_date = ?, end_date = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM vacations WHERE id = ?";
    private static final String FIND_BY_TEACHER_ID = "SELECT id, teacher_id, start_date, end_date from vacations WHERE teacher_id = ?";
    private static final String COUNT_INTERSECTING_VACATIONS = "SELECT count(*) FROM vacations WHERE end_date >= ? AND start_date <= ?";

    private JdbcTemplate jdbcTemplate;
    private VacationMapper vacationMapper;

    public JdbcVacationDao(JdbcTemplate jdbcTemplate, VacationMapper vacationMapper) {
	this.jdbcTemplate = jdbcTemplate;
	this.vacationMapper = vacationMapper;
    }

    @Override
    public int countIntersectingVacations(Vacation vacation) {
	return jdbcTemplate.queryForObject(COUNT_INTERSECTING_VACATIONS, Integer.class, vacation.getStartDate(),
		vacation.getEndDate());
    }

    @Override
    public void create(Vacation vacation) {
	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcTemplate.update(connection -> {
	    PreparedStatement ps = connection
		    .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
	    ps.setObject(1, vacation.getStartDate());
	    ps.setObject(2, vacation.getEndDate());
	    return ps;
	}, keyHolder);
	vacation.setId((int) keyHolder.getKeys().get("id"));
    }

    @Override
    public Optional<Vacation> findByBothDates(Vacation vacation) {
	try {
	    return Optional.of(jdbcTemplate.queryForObject(FIND_BY_BOTH_DATES, vacationMapper, vacation.getStartDate(),
		    vacation.getEndDate()));
	} catch (EmptyResultDataAccessException e) {
	    return Optional.empty();
	}
    }

    @Override
    public Optional<Vacation> findById(int id) {
	try {
	    return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, vacationMapper, id));
	} catch (EmptyResultDataAccessException e) {
	    return Optional.empty();
	}
    }

    @Override
    public List<Vacation> findAll() {
	return jdbcTemplate.query(FIND_ALL, vacationMapper);
    }

    @Override
    public void update(Vacation vacation) {
	jdbcTemplate.update(UPDATE, vacation.getStartDate(), vacation.getEndDate(),
		vacation.getId());
    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public List<Vacation> findByTeacherId(int id) {
	return jdbcTemplate.query(FIND_BY_TEACHER_ID, vacationMapper, id);
    }
}
