package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Holiday;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
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

    public Holiday getById(int id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find holiday by id " + id));
    }

    public void update(Holiday holiday) {
        logger.debug("Updating holiday: {} ", holiday);
        holidayDao.update(holiday);
    }

    public void delete(int id) {
        logger.debug("Deleting holiday by id: {} ", id);
        verifyIdExists(id);
        holidayDao.delete(getById(id));
    }

    private void verifyIdExists(int id) {
        if (holidayDao.findById(id).isEmpty()) {
            throw new EntityNotFoundException(String.format("Holiday id:%s not found, nothing to delete", id));
        }
    }
}
