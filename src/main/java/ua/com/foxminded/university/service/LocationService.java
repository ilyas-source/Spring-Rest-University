package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.LocationDao;
import ua.com.foxminded.university.model.Location;

@Service
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private LocationDao locationDao;
    private ClassroomDao classroomDao;

    public LocationService(LocationDao locationDao, ClassroomDao classroomDao) {
	this.locationDao = locationDao;
	this.classroomDao = classroomDao;
    }

    public void create(Location location) {
	logger.debug("Creating a new location: {} ", location);
	locationDao.create(location);
    }

    public List<Location> findAll() {
	return locationDao.findAll();
    }

    public Optional<Location> findById(int id) {
	return locationDao.findById(id);
    }

    public void update(Location location) {
	logger.debug("Updating location: {} ", location);
	locationDao.update(location);
    }

    private boolean hasNoClassrooms(Location location) {
	return classroomDao.findByLocation(location).isEmpty();
    }

    public void delete(int id) {
	logger.debug("Deleting location by id: {} ", id);
	if (locationDao.findById(id)
		.filter(this::hasNoClassrooms)
		.isPresent()) {
	    locationDao.delete(id);
	}
    }
}
