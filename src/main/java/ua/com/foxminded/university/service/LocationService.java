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

    public Optional<Location> findById(int choice) {
	return locationDao.findById(choice);
    }

    public void update(Location newLocation) {
	locationDao.update(newLocation);
    }

    private boolean locationIsNotUsed(Location location) {
	return classroomDao.findByLocation(location).isEmpty();
    }

    public void delete(int id) {
	boolean canDelete = idExists(id) && locationIsNotUsed(locationDao.findById(id).get());
	if (canDelete) {
	    locationDao.delete(id);
	} else {
	    System.out.println("Can't delete location");
	}
    }

    private boolean idExists(int id) {
	return locationDao.findById(id).isPresent();
    }
}
