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
	if (hasNoIntersections(vacation)) {
	    vacationDao.create(vacation);
	}
    }

    private boolean hasNoIntersections(Vacation vacation) {
	if (vacationDao.findByBothDates(vacation).isPresent()) {
	    return true;
	}
	return vacationDao.countIntersectingVacations(vacation) == 0;
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

    public List<Vacation> findAll() {
	return vacationDao.findAll();
    }

    public Optional<Vacation> findById(int id) {
	return vacationDao.findById(id);
    }

    public void update(Vacation vacation) {
	logger.debug("Updating vacation: {} ", vacation);
	if (hasNoIntersections(vacation)) {
	    vacationDao.update(vacation);
	}
    }

    public void delete(int id) {
	if (idExists(id)) {
	    vacationDao.delete(id);
	}
    }

    private boolean idExists(int id) {
	logger.debug("Deleting vacation by id: {} ", id);
	return vacationDao.findById(id).isPresent();
    }

    public long getDaysDuration(Vacation vacation) {
	return Duration.between(vacation.getStartDate().atStartOfDay(), vacation.getEndDate().atStartOfDay()).toDays() + 1;
    }

}
