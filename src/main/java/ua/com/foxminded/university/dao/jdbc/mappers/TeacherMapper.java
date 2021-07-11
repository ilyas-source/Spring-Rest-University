package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;
import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import ua.com.foxminded.university.dao.jdbc.JdbcVacationDao;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Teacher;

@Component
public class TeacherMapper implements RowMapper<Teacher> {

    private JdbcAddressDao addressDao;
    private JdbcSubjectDao jdbcSubjectDao;
    private JdbcVacationDao jdbcVacationDao;

    public TeacherMapper(JdbcAddressDao addressDao, JdbcSubjectDao jdbcSubjectDao, JdbcVacationDao jdbcVacationDao) {
	this.addressDao = addressDao;
	this.jdbcSubjectDao = jdbcSubjectDao;
	this.jdbcVacationDao = jdbcVacationDao;
    }

    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
	var teacher = new Teacher();
	teacher.setId(rs.getInt("id"));
	teacher.setFirstName(rs.getString("first_name"));
	teacher.setLastName(rs.getString("last_name"));
	teacher.setEmail(rs.getString("email"));
	teacher.setPhoneNumber(rs.getString("phone"));
	teacher.setGender(Gender.valueOf(rs.getString("gender")));
	teacher.setDegree(Degree.valueOf(rs.getString("degree")));
	addressDao.findById(rs.getInt("address_id")).ifPresent(teacher::setAddress);
	teacher.setSubjects(jdbcSubjectDao.getSubjectsByTeacherId(teacher.getId()));
	teacher.setVacations(jdbcVacationDao.getVacationsByTeacherId(teacher.getId()));

	return teacher;
    }
}
