package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.model.Holiday;

@Service
public class HolidayService {

    private HolidayDao holidayDao;

    public HolidayService(HolidayDao holidayDao) {
	this.holidayDao = holidayDao;
    }

    public void create(Holiday holiday) {
	holidayDao.create(holiday);
    }

    public List<Holiday> findAll() {
	return holidayDao.findAll();
    }

    public Optional<Holiday> findById(int id) {
	return holidayDao.findById(id);
    }

    public void update(Holiday holiday) {
	holidayDao.update(holiday);
    }

    public void delete(int id) {
	if (idExists(id)) {
	    holidayDao.delete(id);
	}
    }

    private boolean idExists(int id) {
	return holidayDao.findById(id).isPresent();
    }
}
