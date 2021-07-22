package ua.com.foxminded.university.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.TimeslotDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.TimeslotInUseException;
import ua.com.foxminded.university.exception.TimeslotTooShortException;
import ua.com.foxminded.university.exception.TimeslotsIntersectionException;
import ua.com.foxminded.university.model.Timeslot;

@PropertySource("classpath:university.properties")
@Service
public class TimeslotService {

    private static final Logger logger = LoggerFactory.getLogger(TimeslotService.class);

    private TimeslotDao timeslotDao;
    private LectureDao lectureDao;

    @Value("${timeslot.minimumlength}")
    public int minimumTimeslotLength;

    @Value("${timeslot.minimumbreaklength}")
    public int minimumBreakLength;

    public TimeslotService(TimeslotDao timeslotDao, LectureDao lectureDao) {
	this.timeslotDao = timeslotDao;
	this.lectureDao = lectureDao;
    }

    public void create(Timeslot timeslot) {
	logger.debug("Creating a new timeslot: {} ", timeslot);
	verifyHasNoIntersections(timeslot);
	verifyIsLongEnough(timeslot);
	timeslotDao.create(timeslot);
    }

    public List<Timeslot> findAll() {
	return timeslotDao.findAll();
    }

    public Optional<Timeslot> findById(int id) {
	return timeslotDao.findById(id);
    }

    public void update(Timeslot timeslot) {
	logger.debug("Updating timeslot: {} ", timeslot);
	verifyHasNoIntersections(timeslot);
	verifyIsLongEnough(timeslot);
	timeslotDao.update(timeslot);
    }

    public void delete(int id) {
	logger.debug("Deleting timeslot by id: {} ", id);
	Optional<Timeslot> timeslot = timeslotDao.findById(id);
	if (timeslot.isEmpty()) {
	    throw new EntityNotFoundException(String.format("Timeslot with id=%s not found, nothing to delete", id));
	}
	verifyHasNoLecturesScheduled(timeslot.get());
	timeslotDao.delete(id);
    }

    private void verifyHasNoIntersections(Timeslot timeslot) {
	if (timeslotDao.findByBothTimes(timeslot).isPresent()) {
	    return;
	}
	var timeslotWithBreaks = new Timeslot(timeslot.getBeginTime().minusMinutes(minimumBreakLength),
		timeslot.getEndTime().plusMinutes(minimumBreakLength));
	if (timeslotDao.countIntersectingTimeslots(timeslotWithBreaks) > 0) {
	    throw new TimeslotsIntersectionException(
		    "New timeslot has intersections with existing timetable, can't create/update");
	}
    }

    private void verifyIsLongEnough(Timeslot timeslot) {
	long duration = (Duration.between(timeslot.getBeginTime(), timeslot.getEndTime()).getSeconds()) / 60;
	if (duration < minimumTimeslotLength) {
	    throw new TimeslotTooShortException(String.format(
		    "Minimum timeslot length %s min, but was %s min, can't create timeslot", minimumTimeslotLength, duration));
	}
    }

    private void verifyHasNoLecturesScheduled(Timeslot timeslot) {
	if (!lectureDao.findByTimeslot(timeslot).isEmpty()) {
	    throw new TimeslotInUseException("Timeslot has sheduled lectures, can't delete");
	}
    }
}
