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

import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.dao.jdbc.mappers.HolidayMapper;
import ua.com.foxminded.university.model.Holiday;

@Component
public class JdbcHolidayDao implements HolidayDao {

    private static final String CREATE = "INSERT INTO holidays (date, name) VALUES (?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM holidays WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM holidays";
    private static final String UPDATE = "UPDATE holidays SET date = ?, name = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM holidays WHERE id = ?";

    private JdbcTemplate jdbcTemplate;
    private HolidayMapper holidayMapper;

    public JdbcHolidayDao(JdbcTemplate jdbcTemplate, HolidayMapper holidayMapper) {
	this.jdbcTemplate = jdbcTemplate;
	this.holidayMapper = holidayMapper;
    }

    @Override
    public void create(Holiday holiday) {
	List<Holiday> holidays = findAll();
	if (!holidays.contains(holiday)) {
	    KeyHolder keyHolder = new GeneratedKeyHolder();

	    jdbcTemplate.update(connection -> {
		PreparedStatement ps = connection
			.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
		ps.setObject(1, holiday.getDate());
		ps.setString(2, holiday.getName());
		return ps;
	    }, keyHolder);
	    holiday.setId((int) keyHolder.getKeys().get("id"));
	}
    }

    @Override
    public Optional<Holiday> findById(int id) {
	try {
	    return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, holidayMapper, id));
	} catch (EmptyResultDataAccessException e) {
	    return Optional.empty();
	}
    }

    @Override
    public List<Holiday> findAll() {
	return jdbcTemplate.query(FIND_ALL, holidayMapper);
    }

    @Override
    public void update(Holiday holiday) {
	jdbcTemplate.update(UPDATE, holiday.getDate(), holiday.getName(), holiday.getId());
    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
