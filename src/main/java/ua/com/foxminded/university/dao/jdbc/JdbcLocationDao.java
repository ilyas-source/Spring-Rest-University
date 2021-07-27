package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.LocationDao;
import ua.com.foxminded.university.dao.jdbc.mappers.LocationMapper;
import ua.com.foxminded.university.model.Location;

@Component
public class JdbcLocationDao implements LocationDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcLocationDao.class);

    private static final String CREATE = "INSERT INTO locations (building, floor, room_number) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM locations WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM locations";
    private static final String UPDATE = "UPDATE locations SET building = ?, floor = ?, room_number = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM locations WHERE id = ?";

    private JdbcTemplate jdbcTemplate;
    private LocationMapper locationMapper;

    public JdbcLocationDao(JdbcTemplate jdbcTemplate, LocationMapper locationMapper) {
	this.jdbcTemplate = jdbcTemplate;
	this.locationMapper = locationMapper;
    }

    @Override
    public void create(Location location) {
	logger.debug("Writing a new location to database: {} ", location);
	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcTemplate.update(connection -> {
	    PreparedStatement ps = connection
		    .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
	    ps.setString(1, location.getBuilding());
	    ps.setInt(2, location.getFloor());
	    ps.setInt(3, location.getRoomNumber());

	    return ps;
	}, keyHolder);
	location.setId((int) keyHolder.getKeys().get("id"));
    }

    @Override
    public Optional<Location> findById(int id) {
	logger.debug("Retrieving location by id: {} ", id);
	try {
	    return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, locationMapper, id));
	} catch (EmptyResultDataAccessException e) {
	    return Optional.empty();
	}
    }

    @Override
    public List<Location> findAll() {
	logger.debug("Retrieving all locations");
	return jdbcTemplate.query(FIND_ALL, locationMapper);
    }

    @Override
    public void update(Location location) {
	logger.debug("Updating location in database: {} ", location);
	jdbcTemplate.update(UPDATE, location.getBuilding(), location.getFloor(), location.getRoomNumber(), location.getId());
    }

    @Override
    public void delete(int id) {
	logger.debug("Deleting location by id: {} ", id);
	jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
