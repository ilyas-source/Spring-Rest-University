package ua.com.foxminded.university.dao.jdbc;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.GroupDAO;
import ua.com.foxminded.university.dao.jdbc.mappers.GroupMapper;
import ua.com.foxminded.university.dao.jdbc.mappers.HolidayMapper;
import ua.com.foxminded.university.model.Group;

@Component
public class JdbcGroupDAO implements GroupDAO {

    private static final String CREATE_ = "INSERT INTO groups (name, description) VALUES (?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM groups WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM groups";
    private static final String UPDATE_ = "UPDATE groups SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM groups WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GroupMapper groupMapper;

    @Override
    public void addToDb(Group e) {
	// TODO Auto-generated method stub

    }

    @Override
    public Optional<Group> findById(int id) {
	return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, groupMapper, id));
    }

    @Override
    public List<Group> findAll() {
	return jdbcTemplate.query(FIND_ALL, groupMapper);
    }

    @Override
    public void update(Group e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }

}
