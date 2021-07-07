package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.jdbc.JdbcTimeslotDao;
import ua.com.foxminded.university.model.Timeslot;

@Service
public class TimeslotService {

    private JdbcTimeslotDao jdbcTimeslotDao;

    public TimeslotService(JdbcTimeslotDao jdbcTimeslotDao) {
	this.jdbcTimeslotDao = jdbcTimeslotDao;
    }

    public void create(Timeslot createTimeslot) {
	jdbcTimeslotDao.create(createTimeslot);
    }

    public List<Timeslot> findAll() {
	return jdbcTimeslotDao.findAll();
    }

    public Optional<Timeslot> findById(int choice) {
	return jdbcTimeslotDao.findById(choice);
    }

    public void update(Timeslot newTimeslot) {
	jdbcTimeslotDao.update(newTimeslot);
    }

    public void delete(int id) {
	jdbcTimeslotDao.delete(id);
    }
}
