package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.jdbc.mappers.StudentMapper;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Student;

@Component
public class JdbcStudentDao implements StudentDao {

    private static final String CREATE = "INSERT INTO students (first_name, last_name, gender, birth_date," +
	    " email, phone, address_id, group_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM students WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM students";
    private static final String UPDATE = "UPDATE students SET first_name = ?, last_name = ?, gender = ?, " +
	    " birth_date = ?, email = ?, phone = ?, address_id = ?, group_id = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM students WHERE id = ?";

    private JdbcTemplate jdbcTemplate;
    private StudentMapper studentMapper;
    private JdbcAddressDao jdbcAddressDao;

    public JdbcStudentDao(JdbcTemplate jdbcTemplate, StudentMapper studentMapper, JdbcAddressDao jdbcAddressDao) {
	this.jdbcTemplate = jdbcTemplate;
	this.studentMapper = studentMapper;
	this.jdbcAddressDao = jdbcAddressDao;
    }

    @Override
    public void create(Student student) {
	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcAddressDao.create(student.getAddress());

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
	    ps.setInt(8, student.getGroup().getId());
	    return ps;
	}, keyHolder);
	student.setId((int) keyHolder.getKeys().get("id"));
    }

    @Override
    public void update(Student student) {
	Address address = student.getAddress();
	jdbcAddressDao.update(address);

	jdbcTemplate.update(UPDATE, student.getFirstName(), student.getLastName(),
		new SqlParameterValue(java.sql.Types.OTHER, student.getGender()),
		student.getBirthDate(), student.getEmail(), student.getPhoneNumber(),
		student.getAddress().getId(), student.getGroup().getId(), student.getId());
    }

    @Override
    public Optional<Student> findById(int id) {
	try {
	    return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, studentMapper, id));
	} catch (EmptyResultDataAccessException e) {
	    return Optional.empty();
	}
    }

    @Override
    public List<Student> findAll() {
	return jdbcTemplate.query(FIND_ALL, studentMapper);
    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
