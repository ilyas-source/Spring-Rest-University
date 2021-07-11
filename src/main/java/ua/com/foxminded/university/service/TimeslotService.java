package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.TimeslotDao;
import ua.com.foxminded.university.model.Timeslot;

@Service
public class TimeslotService {

    private TimeslotDao timeslotDao;

    public TimeslotService(TimeslotDao timeslotDao) {
	this.timeslotDao = timeslotDao;
    }

    public void create(Timeslot timeslot) {
	// проверить, что таймслот не пересекает существующие
	// проверить что от других таймслотов минимум 15 минут
	// проверить, что он не короче 15 минут
	timeslotDao.create(timeslot);
    }

    public List<Timeslot> findAll() {
	return timeslotDao.findAll();
    }

    public Optional<Timeslot> findById(int choice) {
	return timeslotDao.findById(choice);
    }

    public void update(Timeslot newTimeslot) {
	// проверить, что таймслот не пересекает существующие
	// проверить что от других таймслотов минимум 15 минут
	// проверить, что он не короче 15 минут
	timeslotDao.update(newTimeslot);
    }

    public void delete(int id) {
	boolean canDelete = idExists(id);
	// проверить, что в этот таймслот не запланированы лекции
	if (canDelete) {
	    timeslotDao.delete(id);
	} else {
	    System.out.println("Can't delete timeslot");
	}
    }

    private boolean idExists(int id) {
	return timeslotDao.findById(id).isPresent();
    }
}
