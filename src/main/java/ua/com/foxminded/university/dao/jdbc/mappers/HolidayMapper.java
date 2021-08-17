package ua.com.foxminded.university.dao.jdbc.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Holiday;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class HolidayMapper implements RowMapper<Holiday> {

    @Override
    public Holiday mapRow(ResultSet rs, int rowNum) throws SQLException {
        var holiday = new Holiday();
        holiday.setId(rs.getInt("id"));
        holiday.setName(rs.getString("name"));
        holiday.setDate(rs.getObject("date", LocalDate.class));

        return holiday;
    }
}
