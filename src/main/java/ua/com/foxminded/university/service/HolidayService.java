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

    public void create(Holiday createHoliday) {
	holidayDao.create(createHoliday);
    }

    public List<Holiday> findAll() {
	return holidayDao.findAll();
    }

    public Optional<Holiday> findById(int choice) {
	return holidayDao.findById(choice);
    }

    public void update(Holiday newHoliday) {
	holidayDao.update(newHoliday);
    }

    public void delete(int id) {
	holidayDao.delete(id);
    }
}
