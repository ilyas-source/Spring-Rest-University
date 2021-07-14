package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.LocationDao;
import ua.com.foxminded.university.model.Location;

@Service
public class LocationService {

    private LocationDao locationDao;
    private ClassroomDao classroomDao;

    public LocationService(LocationDao locationDao, ClassroomDao classroomDao) {
	this.locationDao = locationDao;
	this.classroomDao = classroomDao;
    }

    public void create(Location location) {
	locationDao.create(location);
    }

    public List<Location> findAll() {
	return locationDao.findAll();
    }

    public Optional<Location> findById(int id) {
	return locationDao.findById(id);
    }

    public void update(Location location) {
	locationDao.update(location);
    }

    private boolean hasNoClassrooms(Location location) {
	return classroomDao.findByLocation(location).isEmpty();
    }

    public void delete(int id) {
	Optional<Location> location = locationDao.findById(id);
	boolean canDelete = location.isPresent() && hasNoClassrooms(location.get());
	if (canDelete) {
	    locationDao.delete(id);
	}
    }
}
