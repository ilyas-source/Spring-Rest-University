package ua.com.foxminded.university.dao.jdbc;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.TeacherDAO;
import ua.com.foxminded.university.dao.jdbc.mappers.AddressMapper;
import ua.com.foxminded.university.dao.jdbc.mappers.TeacherMapper;
import ua.com.foxminded.university.model.Teacher;

@Component
public class JdbcTeacherDAO implements TeacherDAO {

    private static final String CREATE_ = "INSERT INTO teachers (name, description) VALUES (?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM teachers WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM teachers";
    private static final String UPDATE_ = "UPDATE teachers SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM teachers WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public void addToDb(Teacher e) {
	// TODO Auto-generated method stub

    }

    @Override
    public Optional<Teacher> findById(int id) {
	return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, teacherMapper, id));
    }

    @Override
    public List<Teacher> findAll() {
	return jdbcTemplate.query(FIND_ALL, teacherMapper);
    }

    @Override
    public void update(Teacher e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }

}
