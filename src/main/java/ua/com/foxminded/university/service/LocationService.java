package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.LocationDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.LocationInUseException;
import ua.com.foxminded.university.model.Classroom;
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

    public void delete(int id) {
	logger.debug("Deleting location by id: {} ", id);
	Optional<Location> location = locationDao.findById(id);
	if (location.isEmpty()) {
	    throw new EntityNotFoundException(String.format("Location with id=%s not found, nothing to delete", id));
	}
	verifyIsNotUsed(location.get());
	locationDao.delete(id);
    }

    private void verifyIsNotUsed(Location location) {
	Optional<Classroom> classroom = classroomDao.findByLocation(location);
	if (classroom.isPresent()) {
	    throw new LocationInUseException("Location is used for " + classroom.get().getName());
	}
    }
}
