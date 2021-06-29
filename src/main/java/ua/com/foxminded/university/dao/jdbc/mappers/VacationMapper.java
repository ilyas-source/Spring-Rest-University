package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

@Component
public class VacationMapper implements RowMapper<Vacation> {

    private JdbcTeacherDao jdbcTeacherDao;

    @Override
    public Vacation mapRow(ResultSet rs, int rowNum) throws SQLException {
	Vacation vacation = new Vacation();
	vacation.setId(rs.getInt("id"));
	jdbcTeacherDao.findById(rs.getInt("teacher_id")).ifPresent(vacation::setTeacher);
	vacation.setStartDate(rs.getObject("start_date", LocalDate.class));
	vacation.setEndDate(rs.getObject("end_date", LocalDate.class));

	return vacation;
    }
}
