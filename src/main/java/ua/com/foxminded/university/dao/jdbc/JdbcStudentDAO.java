package ua.com.foxminded.university.dao.jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.StudentDAO;
import ua.com.foxminded.university.dao.jdbc.mappers.AddressMapper;
import ua.com.foxminded.university.dao.jdbc.mappers.StudentMapper;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;

//CREATE TABLE students (
//id integer NOT NULL generated BY DEFAULT AS identity,
//first_name VARCHAR(255),
//last_name VARCHAR(255),
//gender GENDER,
//birth_date DATE,
//entry_year DATE,
//email VARCHAR(255),
//phone VARCHAR(255),
//address_id INTEGER REFERENCES addresses(id) ON DELETE CASCADE
//);

@Component
public class JdbcStudentDAO implements StudentDAO {

    private static final String CREATE = "INSERT INTO students (first_name, last_name, gender, birth_date," +
	    " email, phone, address_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM students WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM students";
    private static final String UPDATE_ = "UPDATE students SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM students WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private JdbcAddressDAO jdbcAddressDAO;

    @Override
    public void addToDb(Student student) {
	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcAddressDAO.addToDb(student.getAddress());

	jdbcTemplate.update(connection -> {
	    PreparedStatement ps = connection
		    .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
	    ps.setString(1, student.getFirstName());
	    ps.setString(2, student.getLastName());
	    ps.setObject(3, student.getGender(), java.sql.Types.OTHER);
	    ps.setObject(4, student.getBirthDate());
	    ps.setString(5, student.getEmail());
	    ps.setString(6, student.getPhoneNumber());
	    ps.setInt(7, student.getAddress().getId());
	    return ps;
	}, keyHolder);
	student.setId((int) keyHolder.getKeys().get("id"));
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
