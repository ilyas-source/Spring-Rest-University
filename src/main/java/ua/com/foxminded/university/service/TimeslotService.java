package ua.com.foxminded.university.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.TimeslotDao;
import ua.com.foxminded.university.model.Timeslot;

@PropertySource("classpath:university.properties")
@Service
public class TimeslotService {

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
	if (hasNoIntersections(timeslot) && isLongEnough(timeslot)) {
	    timeslotDao.create(timeslot);
	}
    }

    private boolean hasNoIntersections(Timeslot timeslot) {
	return timeslotDao.findAll()
		.stream()
		.flatMap(t -> Stream.of(t.intersects(timeslot, minimumBreakLength * 60)))
		.noneMatch(b -> b);
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
	boolean canUpdate = hasNoIntersections(timeslot) && isLongEnough(timeslot);
	if (canUpdate) {
	    timeslotDao.update(timeslot);
	}
    }

    public void delete(int id) {
	Optional<Timeslot> timeslot = timeslotDao.findById(id);
	boolean canDelete = timeslot.isPresent() && hasNoLecturesScheduled(timeslot.get());
	if (canDelete) {
	    timeslotDao.delete(id);
	}
    }

    private boolean hasNoLecturesScheduled(Timeslot timeslot) {
	return lectureDao.findByTimeslot(timeslot).isEmpty();
    }
}
