package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;
import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import ua.com.foxminded.university.dao.jdbc.JdbcVacationDao;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

@Component
public class TeacherMapper implements RowMapper<Teacher> {

    private JdbcAddressDao jdbcAddressDao;
    private JdbcSubjectDao jdbcSubjectDao;
    private JdbcVacationDao jdbcVacationDao;

    public TeacherMapper(JdbcAddressDao jdbcAddressDao, JdbcSubjectDao jdbcSubjectDao, JdbcVacationDao jdbcVacationDao) {
	this.jdbcAddressDao = jdbcAddressDao;
	this.jdbcSubjectDao = jdbcSubjectDao;
	this.jdbcVacationDao = jdbcVacationDao;
    }

    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
	Teacher teacher = new Teacher();
	teacher.setId(rs.getInt("id"));
	teacher.setFirstName(rs.getString("first_name"));
	teacher.setLastName(rs.getString("last_name"));
	teacher.setEmail(rs.getString("email"));
	teacher.setPhoneNumber(rs.getString("phone"));

	teacher.setGender(Gender.valueOf(rs.getString("gender")));
	teacher.setDegree(Degree.valueOf(rs.getString("degree")));

	Address address = jdbcAddressDao.findById(rs.getInt("address_id")).orElse(null);
	teacher.setAddress(address);

	List<Subject> subjects = jdbcSubjectDao.getSubjectsByTeacher(teacher.getId());
	teacher.setSubjects(subjects);

	List<Vacation> vacations = jdbcVacationDao.getVacationsByTeacher(teacher.getId());
	teacher.setVacations(vacations);
	return teacher;
    }
}
