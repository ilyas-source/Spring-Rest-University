package ua.com.foxminded.university.dao.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.dao.jdbc.mappers.TimeslotMapper;
import ua.com.foxminded.university.model.Timeslot;

public class JdbcTimeslotDao {

    private static final String COUNT_INTERSECTING_TIMESLOTS = "SELECT count(*) FROM timeslots WHERE end_time >= ? AND begin_time <= ?";

    private JdbcTemplate jdbcTemplate;
    private TimeslotMapper timeslotMapper;

    public JdbcTimeslotDao(JdbcTemplate jdbcTemplate, TimeslotMapper timeslotMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.timeslotMapper = timeslotMapper;
    }

    public int countIntersectingTimeslots(Timeslot timeslot) {
        return jdbcTemplate.queryForObject(COUNT_INTERSECTING_TIMESLOTS, Integer.class, timeslot.getBeginTime(),
                timeslot.getEndTime());
    }
}
