package ua.com.foxminded.university.dao.jdbc;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.StudentDAO;
import ua.com.foxminded.university.dao.jdbc.mappers.AddressMapper;
import ua.com.foxminded.university.dao.jdbc.mappers.StudentMapper;
import ua.com.foxminded.university.model.Student;

@Component
public class JdbcStudentDAO implements StudentDAO {

    private static final String CREATE_ = "INSERT INTO students (name, description) VALUES (?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM students WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM students";
    private static final String UPDATE_ = "UPDATE students SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM students WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public void addToDb(Student e) {
	// TODO Auto-generated method stub

    }

    @Override
    public Optional<Student> findById(int id) {
	return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, studentMapper, id));
    }

    @Override
    public List<Student> findAll() {
	return jdbcTemplate.query(FIND_ALL, studentMapper);
    }

    @Override
    public void update(Student e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }

}
