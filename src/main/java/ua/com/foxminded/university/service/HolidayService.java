package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.model.Holiday;

@Service
public class HolidayService {

    private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);

    private HolidayDao holidayDao;

    public HolidayService(HolidayDao holidayDao) {
	this.holidayDao = holidayDao;
    }

    public void create(Holiday holiday) {
	logger.debug("Creating a new holiday: {} ", holiday);
	holidayDao.create(holiday);
    }

    public List<Holiday> findAll() {
	return holidayDao.findAll();
    }

    public Optional<Holiday> findById(int id) {
	return holidayDao.findById(id);
    }

    public void update(Holiday holiday) {
	logger.debug("Updating holiday: {} ", holiday);
	holidayDao.update(holiday);
    }

    public void delete(int id) {
	logger.debug("Deleting holiday by id: {} ", id);
	if (idExists(id)) {
	    holidayDao.delete(id);
	}
    }

    private boolean idExists(int id) {
	return holidayDao.findById(id).isPresent();
    }
}
