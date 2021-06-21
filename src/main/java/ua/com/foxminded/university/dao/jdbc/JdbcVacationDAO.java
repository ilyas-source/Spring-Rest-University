package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.VacationDAO;
import ua.com.foxminded.university.dao.jdbc.mappers.VacationMapper;
import ua.com.foxminded.university.model.Vacation;

@Component
public class JdbcVacationDAO implements VacationDAO {

    private static final String CREATE = "INSERT INTO vacations (start_date, end_date) VALUES (?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM vacations WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM vacations";
    private static final String UPDATE = "UPDATE vacations SET start_date = ?, end_date = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM vacations WHERE id = ?";
    private static final String FIND_BY_TEACHER_ID = "SELECT v.id, v.start_date, v.end_date from teachers_vacations " +
	    "AS t_v LEFT JOIN vacations AS v ON (t_v.vacation_id=v.id) WHERE t_v.teacher_id=?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private VacationMapper vacationMapper;

    @Override
    public void addToDb(Vacation vacation) {
	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcTemplate.update(connection -> {
	    PreparedStatement ps = connection
		    .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
	    ps.setObject(1, vacation.getStartDate());
	    ps.setObject(2, vacation.getEndDate());
	    return ps;
	}, keyHolder);
	vacation.setId((int) keyHolder.getKeys().get("id"));
    }

    @Override
    public Optional<Vacation> findById(int id) {
	return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, vacationMapper, id));
    }

    @Override
    public List<Vacation> findAll() {
	return jdbcTemplate.query(FIND_ALL, vacationMapper);
    }

    @Override
    public void update(Vacation vacation) {
	jdbcTemplate.update(UPDATE, vacation.getStartDate(), vacation.getEndDate(), vacation.getId());
    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }

    public List<Vacation> getVacationsByTeacher(int id) {
	return jdbcTemplate.query(FIND_BY_TEACHER_ID, vacationMapper, id);
    }
}
