package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Student;

@Component
public class StudentMapper implements RowMapper<Student> {

    private AddressDao jdbcAddressDao;
    private JdbcGroupDao jdbcGroupDao;

    public StudentMapper(AddressDao jdbcAddressDao, JdbcGroupDao jdbcGroupDao) {
	this.jdbcAddressDao = jdbcAddressDao;
	this.jdbcGroupDao = jdbcGroupDao;
    }

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
	var student = new Student();
	student.setId(rs.getInt("id"));
	jdbcAddressDao.findById(rs.getInt("address_id")).ifPresent(student::setAddress);
	student.setBirthDate(rs.getObject("birth_date", LocalDate.class));
	student.setEmail(rs.getString("email"));
	student.setFirstName(rs.getString("first_name"));
	student.setPhoneNumber(rs.getString("phone"));
	student.setLastName(rs.getString("last_name"));
	student.setGender(Gender.valueOf(rs.getString("gender")));
	jdbcGroupDao.findById(rs.getInt("group_id")).ifPresent(student::setGroup);

	return student;
    }
}
