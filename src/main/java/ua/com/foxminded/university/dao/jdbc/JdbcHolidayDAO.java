package ua.com.foxminded.university.dao.jdbc;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.HolidayDAO;
import ua.com.foxminded.university.dao.jdbc.mappers.HolidayMapper;
import ua.com.foxminded.university.model.Holiday;

@Component
public class JdbcHolidayDAO implements HolidayDAO {

    private static final String CREATE = "INSERT INTO holidays (name, description) VALUES (?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM holidays WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM holidays";
    private static final String UPDATE_ = "UPDATE holidays SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM holidays WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private HolidayMapper holidayMapper;

    @Override
    public void addToDb(Holiday e) {
	// TODO Auto-generated method stub

    }

    @Override
    public Optional<Holiday> findById(int id) {
	return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, holidayMapper, id));
    }

    @Override
    public List<Holiday> findAll() {
	return jdbcTemplate.query(FIND_ALL, holidayMapper);
    }

    @Override
    public void update(Holiday e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }

}
