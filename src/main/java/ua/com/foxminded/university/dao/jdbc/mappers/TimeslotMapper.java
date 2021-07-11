package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.model.Timeslot;

@Component
public class TimeslotMapper implements RowMapper<Timeslot> {

    @Override
    public Timeslot mapRow(ResultSet rs, int rowNum) throws SQLException {
	var timeslot = new Timeslot();
	timeslot.setId(rs.getInt("id"));
	timeslot.setBeginTime(rs.getObject("begin_time", LocalTime.class));
	timeslot.setEndTime(rs.getObject("end_time", LocalTime.class));
	return timeslot;
    }
}
