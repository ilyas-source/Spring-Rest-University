package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.jdbc.JdbcLocationDao;
import ua.com.foxminded.university.model.Location;

@Service
public class LocationService {

    private JdbcLocationDao jdbcLocationDao;

    public LocationService(JdbcLocationDao jdbcLocationDao) {
	this.jdbcLocationDao = jdbcLocationDao;
    }

    public void create(Location createLocation) {
	jdbcLocationDao.create(createLocation);
    }

    public List<Location> findAll() {
	return jdbcLocationDao.findAll();
    }

    public Optional<Location> findById(int choice) {
	return jdbcLocationDao.findById(choice);
    }

    public void update(Location newLocation) {
	jdbcLocationDao.update(newLocation);
    }

    public void delete(int id) {
	// проверить что в этой локации нет аудиторий
	jdbcLocationDao.delete(id);
    }
}
