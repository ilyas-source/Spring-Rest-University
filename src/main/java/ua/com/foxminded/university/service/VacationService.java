package ua.com.foxminded.university.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.VacationsIntersectionException;
import ua.com.foxminded.university.model.Vacation;

@Service
public class VacationService {

    private static final Logger logger = LoggerFactory.getLogger(VacationService.class);

    private VacationDao vacationDao;

    public VacationService(VacationDao vacationDao) {
	this.vacationDao = vacationDao;
    }

    public void create(Vacation vacation) {
	logger.debug("Creating a new vacation: {} ", vacation);
	verifyHasNoIntersections(vacation);
	vacationDao.create(vacation);
    }

    public List<Vacation> findAll() {
	return vacationDao.findAll();
    }

    public Optional<Vacation> findById(int id) {
	return vacationDao.findById(id);
    }

    public void update(Vacation vacation) {
	logger.debug("Updating vacation: {} ", vacation);
	verifyHasNoIntersections(vacation);
	vacationDao.update(vacation);
    }

    public void delete(int id) {
	logger.debug("Deleting vacation by id: {} ", id);
	verifyIdExists(id);
	vacationDao.delete(id);
    }

    private void verifyHasNoIntersections(Vacation vacation) {
	if (vacationDao.findByBothDates(vacation).isPresent()) {
	    return;
	}
	if (vacationDao.countIntersectingVacations(vacation) > 0) {
	    throw new VacationsIntersectionException(
		    "New vacation has intersections with existing vacations, can't create/update");
	}
    }

    public Map<Integer, Long> countDaysByYears(List<Vacation> vacations) {
	Map<Integer, Long> result = new HashMap<>();
	for (Vacation v : vacations) {
	    LocalDate startDate = v.getStartDate();
	    LocalDate endDate = v.getEndDate();
	    int startYear = startDate.getYear();
	    int endYear = endDate.getYear();
	    if (startYear == endYear) {
		incrementValue(result, startYear, getDaysDuration(v));
	    } else {
		long remainingDays = Duration.between(startDate.atStartOfDay(), LocalDate.of(startYear, 12, 31).atStartOfDay())
			.toDays();
		long trailingDays = Duration.between(LocalDate.of(endYear, 1, 1).atStartOfDay(), endDate.atStartOfDay())
			.toDays();
		incrementValue(result, startYear, remainingDays);
		incrementValue(result, endYear, trailingDays);
	    }
	}
	return result;
    }

    private void incrementValue(Map<Integer, Long> result, int key, long increment) {
	long currentValue = 0;
	if (result.containsKey(key)) {
	    currentValue = result.get(key);
	}
	result.put(key, currentValue + increment);
    }

    private void verifyIdExists(int id) {
	if (vacationDao.findById(id).isEmpty()) {
	    throw new EntityNotFoundException(String.format("Vacation with id=%s not found, nothing to delete", id));
	}
    }

    public long getDaysDuration(Vacation vacation) {
	return Duration.between(vacation.getStartDate().atStartOfDay(), vacation.getEndDate().atStartOfDay()).toDays() + 1;
    }
}
