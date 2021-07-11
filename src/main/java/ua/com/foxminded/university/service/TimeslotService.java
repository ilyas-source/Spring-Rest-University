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
	boolean canCreate = noIntersections(timeslot);
	canCreate = canCreate && longEnough(timeslot);

	if (canCreate) {
	    timeslotDao.create(timeslot);
	} else {
	    System.out.println("Can't create this timeslot");
	}
    }

    private boolean noIntersections(Timeslot timeslot) {
	boolean result = timeslotDao.findAll()
		.stream()
		.flatMap(t -> Stream.of(t.intersects(timeslot)))
		.filter(b -> b == true)
		.findFirst()
		.isEmpty();
	System.out.println("No intersections with existing timeslots: " + result);
	return result;
    }

    private boolean longEnough(Timeslot timeslot) {
	boolean result = Duration.between(timeslot.getBeginTime(), timeslot.getEndTime()).getSeconds() >= 900;
	System.out.println("New timeslot is long enough: " + result);

	return result;
    }

    public List<Timeslot> findAll() {
	return timeslotDao.findAll();
    }

    public Optional<Timeslot> findById(int choice) {
	return timeslotDao.findById(choice);
    }

    public void update(Timeslot timeslot) {
	boolean canUpdate = noIntersections(timeslot) && longEnough(timeslot);
	if (canUpdate) {
	    timeslotDao.update(timeslot);
	} else {
	    System.out.println("Can't update timeslot");
	}
    }

    public void delete(int id) {
	boolean canDelete = idExists(id) && noLecturesScheduled(timeslotDao.findById(id).get());
	if (canDelete) {
	    timeslotDao.delete(id);
	} else {
	    System.out.println("Can't delete timeslot");
	}
    }

    private boolean noLecturesScheduled(Timeslot timeslot) {
	boolean result = lectureDao.findByTimeslot(timeslot).isEmpty();
	System.out.println("No lectures scheduled to timeslot: " + result);
	return result;
    }

    private boolean idExists(int id) {
	return timeslotDao.findById(id).isPresent();
    }
}
