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
	if (hasNoIntersections(timeslot) && isLongEnough(timeslot)) {
	    timeslotDao.create(timeslot);
	}
    }

    private boolean hasNoIntersections(Timeslot timeslot) {
	if (timeslotDao.findByBothTimes(timeslot).isPresent()) {
	    return true;
	}
	var timeslotWithBreaks = new Timeslot(timeslot.getBeginTime().minusMinutes(minimumBreakLength),
		timeslot.getEndTime().plusMinutes(minimumBreakLength));
	return timeslotDao.countIntersectingTimeslots(timeslotWithBreaks) == 0;
    }

    private boolean isLongEnough(Timeslot timeslot) {
	return Duration.between(timeslot.getBeginTime(), timeslot.getEndTime()).getSeconds() >= minimumTimeslotLength * 60;
    }

    public List<Timeslot> findAll() {
	return timeslotDao.findAll();
    }

    public Optional<Timeslot> findById(int id) {
	return timeslotDao.findById(id);
    }

    public void update(Timeslot timeslot) {
	logger.debug("Updating timeslot: {} ", timeslot);
	if (hasNoIntersections(timeslot) && isLongEnough(timeslot)) {
	    timeslotDao.update(timeslot);
	}
    }

    public void delete(int id) {
	logger.debug("Deleting timeslot by id: {} ", id);
	if (timeslotDao.findById(id)
		.filter(this::hasNoLecturesScheduled)
		.isPresent()) {
	    timeslotDao.delete(id);
	}
    }

    private boolean hasNoLecturesScheduled(Timeslot timeslot) {
	return lectureDao.findByTimeslot(timeslot).isEmpty();
    }
}
