package ua.com.foxminded.university.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.TimeslotDao;
import ua.com.foxminded.university.model.Timeslot;

@Service
public class TimeslotService {

    private TimeslotDao timeslotDao;
    private LectureDao lectureDao;

    public TimeslotService(TimeslotDao timeslotDao, LectureDao lectureDao) {
	this.timeslotDao = timeslotDao;
	this.lectureDao = lectureDao;
    }

    public void create(Timeslot timeslot) {
	boolean canCreate = hasNoIntersections(timeslot)
		&& isLongEnough(timeslot);
	if (canCreate) {
	    timeslotDao.create(timeslot);
	}
    }

    private boolean hasNoIntersections(Timeslot timeslot) {
	return timeslotDao.findAll()
		.stream()
		.flatMap(t -> Stream.of(t.intersects(timeslot)))
		.filter(b -> b == true)
		.findFirst()
		.isEmpty();
    }

    private boolean isLongEnough(Timeslot timeslot) {
	return Duration.between(timeslot.getBeginTime(), timeslot.getEndTime()).getSeconds() >= 900;
    }

    public List<Timeslot> findAll() {
	return timeslotDao.findAll();
    }

    public Optional<Timeslot> findById(int choice) {
	return timeslotDao.findById(choice);
    }

    public void update(Timeslot timeslot) {
	boolean canUpdate = hasNoIntersections(timeslot) && isLongEnough(timeslot);
	if (canUpdate) {
	    timeslotDao.update(timeslot);
	}
    }

    public void delete(int id) {

	Optional<Timeslot> optionalTimeslot = timeslotDao.findById(id);
	boolean canDelete = optionalTimeslot.isPresent() && hasNoLecturesScheduled(optionalTimeslot.get());
	if (canDelete) {
	    timeslotDao.delete(id);
	}
    }

    private boolean hasNoLecturesScheduled(Timeslot timeslot) {
	return lectureDao.findByTimeslot(timeslot).isEmpty();
    }
}
