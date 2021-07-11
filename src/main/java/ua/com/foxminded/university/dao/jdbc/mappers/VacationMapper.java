package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.model.Vacation;

@Component
public class VacationMapper implements RowMapper<Vacation> {

    @Override
    public Vacation mapRow(ResultSet rs, int rowNum) throws SQLException {
	var vacation = new Vacation();
	vacation.setId(rs.getInt("id"));
	vacation.setStartDate(rs.getObject("start_date", LocalDate.class));
	vacation.setEndDate(rs.getObject("end_date", LocalDate.class));

	return vacation;
    }
}
