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

    @Override
    public Vacation mapRow(ResultSet rs, int rowNum) throws SQLException {
	Vacation vacation = new Vacation();
	vacation.setId(rs.getInt("id"));

	LocalDate startDate = rs.getObject("start_date", LocalDate.class);
	LocalDate endDate = rs.getObject("end_date", LocalDate.class);

	vacation.setStartDate(startDate);
	vacation.setEndDate(endDate);

	return vacation;
    }
}
