package ua.com.foxminded.university.dao.jdbc;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.VacationDAO;
import ua.com.foxminded.university.dao.jdbc.mappers.AddressMapper;
import ua.com.foxminded.university.dao.jdbc.mappers.VacationMapper;
import ua.com.foxminded.university.model.Vacation;

@Component
public class JdbcVacationDAO implements VacationDAO {

    private static final String CREATE_ = "INSERT INTO vacations (name, description) VALUES (?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM vacations WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM vacations";
    private static final String UPDATE_ = "UPDATE vacations SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM vacations WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private VacationMapper vacationMapper;

    @Override
    public void addToDb(Vacation e) {
	// TODO Auto-generated method stub
    }

    @Override
    public Optional<Vacation> findById(int id) {
	return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, vacationMapper, id));
    }

    @Override
    public List<Vacation> findAll() {
	return jdbcTemplate.query(FIND_ALL, vacationMapper);
    }

    @Override
    public void update(Vacation e) {
	// TODO Auto-generated method stub
    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }

}
