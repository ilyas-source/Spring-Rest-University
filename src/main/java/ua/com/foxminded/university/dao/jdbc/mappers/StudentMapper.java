package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    private JdbcAddressDao jdbcAddressDAO;
    private JdbcGroupDao jdbcGroupDAO;

    public StudentMapper(JdbcAddressDao jdbcAddressDAO, JdbcGroupDao jdbcGroupDAO) {
	this.jdbcAddressDAO = jdbcAddressDAO;
	this.jdbcGroupDAO = jdbcGroupDAO;
    }

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
	Student student = new Student();

	student.setId(rs.getInt("id"));

	Address address = jdbcAddressDAO.findById(rs.getInt("address_id")).orElse(null);
	student.setAddress(address);

	student.setBirthDate(rs.getDate("birth_date").toLocalDate());
	student.setEmail(rs.getString("email"));
	student.setFirstName(rs.getString("first_name"));
	student.setPhoneNumber(rs.getString("phone"));
	student.setLastName(rs.getString("last_name"));
	student.setGender(Gender.valueOf(rs.getString("gender")));

	Group group = jdbcGroupDAO.findById(rs.getInt("group_id")).orElse(null);
	student.setGroup(group);

	return student;
    }
}
