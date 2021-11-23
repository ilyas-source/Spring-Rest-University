package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.repository.HolidayRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class HolidayService {

    private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);

    private HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    public void create(Holiday holiday) {
        logger.debug("Creating a new holiday: {} ", holiday);
        holidayRepository.save(holiday);
    }

    public List<Holiday> findAll() {
        return holidayRepository.findAll();
    }

    public Optional<Holiday> findById(int id) {
        return holidayRepository.findById(id);
    }

    public Holiday getById(int id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find holiday by id " + id));
    }

    public void update(Holiday holiday) {
        logger.debug("Updating holiday: {} ", holiday);
        holidayRepository.save(holiday);
    }

    public void delete(int id) {
        logger.debug("Deleting holiday by id: {} ", id);
        verifyIdExists(id);
        holidayRepository.delete(getById(id));
    }

    private void verifyIdExists(int id) {
        if (holidayRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(String.format("Holiday id:%s not found, nothing to delete", id));
        }
    }
}
