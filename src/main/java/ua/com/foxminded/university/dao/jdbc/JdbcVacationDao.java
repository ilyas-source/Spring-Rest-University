package ua.com.foxminded.university.dao.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.dao.jdbc.mappers.VacationMapper;
import ua.com.foxminded.university.model.Vacation;

import java.util.List;


public class JdbcVacationDao  {

    private static final String FIND_BY_TEACHER_ID = "SELECT id, teacher_id, start_date, end_date from vacations WHERE teacher_id = ?";
    private static final String COUNT_INTERSECTING_VACATIONS = "SELECT count(*) FROM vacations WHERE end_date >= ? AND start_date <= ?";

    private JdbcTemplate jdbcTemplate;
    private VacationMapper vacationMapper;

    public JdbcVacationDao(JdbcTemplate jdbcTemplate, VacationMapper vacationMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.vacationMapper = vacationMapper;
    }

    public List<Vacation> findByTeacherId(int id) {
        return jdbcTemplate.query(FIND_BY_TEACHER_ID, vacationMapper, id);
    }
}
