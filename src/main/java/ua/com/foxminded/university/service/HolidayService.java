package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import ua.com.foxminded.university.model.Holiday;

@Service
public class HolidayService {

    private JdbcHolidayDao jdbcHolidayDao;

    public HolidayService(JdbcHolidayDao jdbcHolidayDao) {
	this.jdbcHolidayDao = jdbcHolidayDao;
    }

    public void create(Holiday createHoliday) {
	// проверить что на эту дату не запланирован другой праздник
	jdbcHolidayDao.create(createHoliday);
    }

    public List<Holiday> findAll() {
	return jdbcHolidayDao.findAll();
    }

    public Optional<Holiday> findById(int choice) {
	return jdbcHolidayDao.findById(choice);
    }

    public void update(Holiday newHoliday) {
	// проверить что на эту дату не запланирован другой праздник
	jdbcHolidayDao.update(newHoliday);
    }

    public void delete(int id) {
	jdbcHolidayDao.delete(id);
    }
}
