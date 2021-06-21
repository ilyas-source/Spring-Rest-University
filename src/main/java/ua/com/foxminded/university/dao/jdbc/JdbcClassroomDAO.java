package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.ClassroomDAO;
import ua.com.foxminded.university.dao.jdbc.mappers.ClassroomMapper;
import ua.com.foxminded.university.model.Classroom;

@Component
public class JdbcClassroomDAO implements ClassroomDAO {

    private static final String CREATE = "INSERT INTO classrooms (location_id, name, capacity) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM classrooms WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM classrooms";
    private static final String UPDATE = "UPDATE classrooms SET location_id = ?, name = ?, capacity = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM classrooms WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ClassroomMapper classroomMapper;
    @Autowired
    private JdbcLocationDAO jdbcLocationDAO;

    @Override
    public void addToDb(Classroom classroom) {
	jdbcLocationDAO.addToDb(classroom.getLocation());

	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcTemplate.update(connection -> {
	    PreparedStatement ps = connection
		    .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
	    ps.setInt(1, classroom.getLocation().getId());
	    ps.setString(2, classroom.getName());
	    ps.setInt(3, classroom.getCapacity());
	    return ps;
	}, keyHolder);
	classroom.setId((int) keyHolder.getKeys().get("id"));
    }

    @Override
    public Optional<Classroom> findById(int id) {
	try {
	    return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, classroomMapper, id));
	} catch (EmptyResultDataAccessException e) {
	    return Optional.empty();
	}
    }

    @Override
    public List<Classroom> findAll() {
	return jdbcTemplate.query(FIND_ALL, classroomMapper);
    }

    @Override
    public void update(Classroom classroom) {
	jdbcLocationDAO.addToDb(classroom.getLocation());
	jdbcTemplate.update(UPDATE, classroom.getLocation().getId(), classroom.getName(), classroom.getCapacity(),
		classroom.getId());
    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
