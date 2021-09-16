package ua.com.foxminded.university.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.LocationDao;
import ua.com.foxminded.university.dao.jdbc.mappers.ClassroomMapper;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcClassroomDao implements ClassroomDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcClassroomDao.class);

    private static final String CREATE = "INSERT INTO classrooms (location_id, name, capacity) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM classrooms WHERE id = ?";
    private static final String FIND_BY_NAME = "SELECT * FROM classrooms WHERE name = ?";
    private static final String FIND_BY_LOCATION_ID = "SELECT * FROM classrooms WHERE location_id = ?";
    private static final String FIND_ALL = "SELECT * FROM classrooms";
    private static final String UPDATE = "UPDATE classrooms SET location_id = ?, name = ?, capacity = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM classrooms WHERE id = ?";

    private JdbcTemplate jdbcTemplate;
    private ClassroomMapper classroomMapper;
    private LocationDao locationDao;

    public JdbcClassroomDao(JdbcTemplate jdbcTemplate, ClassroomMapper classroomMapper, LocationDao locationDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.classroomMapper = classroomMapper;
        this.locationDao = locationDao;
    }

    @Override
    @Transactional
    public void create(Classroom classroom) {
        logger.debug("Writing a new classroom to database: {} ", classroom);
        locationDao.create(classroom.getLocation());
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
        logger.debug("Retrieving classroom by id: {} ", id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, classroomMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Classroom> findAll() {
        logger.debug("Retrieving all classrooms");
        return jdbcTemplate.query(FIND_ALL, classroomMapper);
    }

    @Override
    @Transactional
    public void update(Classroom classroom) {
        logger.debug("Updating classroom in database: {} ", classroom);
        locationDao.update(classroom.getLocation());
        jdbcTemplate.update(UPDATE, classroom.getLocation().getId(), classroom.getName(), classroom.getCapacity(),
                classroom.getId());
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting classroom by id: {} ", id);
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public Optional<Classroom> findByName(String name) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_BY_NAME, classroomMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Classroom> findByLocation(Location location) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_BY_LOCATION_ID, classroomMapper, location.getId()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
