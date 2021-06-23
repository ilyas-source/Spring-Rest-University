package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

@Component
public class StudentMapper implements RowMapper<Student> {

    private JdbcAddressDao jdbcAddressDao;
    private JdbcGroupDao jdbcGroupDao;

    public StudentMapper(JdbcAddressDao jdbcAddressDao, JdbcGroupDao jdbcGroupDao) {
	this.jdbcAddressDao = jdbcAddressDao;
	this.jdbcGroupDao = jdbcGroupDao;
    }

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
	Student student = new Student();

	student.setId(rs.getInt("id"));

	Address address = jdbcAddressDao.findById(rs.getInt("address_id")).orElseThrow();
	student.setAddress(address);
	LocalDate birthDate = rs.getObject("birth_date", LocalDate.class);
	student.setBirthDate(birthDate);
	student.setEmail(rs.getString("email"));
	student.setFirstName(rs.getString("first_name"));
	student.setPhoneNumber(rs.getString("phone"));
	student.setLastName(rs.getString("last_name"));
	student.setGender(Gender.valueOf(rs.getString("gender")));

	Group group = jdbcGroupDao.findById(rs.getInt("group_id")).orElseThrow();
	student.setGroup(group);

	return student;
    }
}
