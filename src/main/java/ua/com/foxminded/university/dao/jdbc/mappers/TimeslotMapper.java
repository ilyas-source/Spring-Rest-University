package ua.com.foxminded.university.dao.jdbc.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Timeslot;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TimeslotMapper implements RowMapper<Timeslot> {

    @Override
    public Timeslot mapRow(ResultSet rs, int rowNum) throws SQLException {
        var timeslot = new Timeslot();
        timeslot.setId(rs.getInt("id"));
        timeslot.setBeginTime(rs.getTime("begin_time").toLocalTime());
        timeslot.setEndTime(rs.getTime("end_time").toLocalTime());

        return timeslot;
    }
}
