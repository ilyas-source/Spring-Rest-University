package ua.com.foxminded.university.menu;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

import static ua.com.foxminded.university.Menu.*;

import ua.com.foxminded.university.dao.jdbc.JdbcTimeslotDao;
import ua.com.foxminded.university.model.Timeslot;

@Component
public class TimeslotsMenu {

    JdbcTimeslotDao jdbcTimeslotDao;

    public TimeslotsMenu(JdbcTimeslotDao jdbcTimeslotDao) {
	this.jdbcTimeslotDao = jdbcTimeslotDao;
    }

    public String getStringOfTimeslots(List<Timeslot> timeslots) {
	StringBuilder result = new StringBuilder();
	timeslots.sort(Comparator.comparing(Timeslot::getId));

	for (Timeslot timeslot : timeslots) {
	    result.append(getStringFromTimeslot(timeslot) + CR);
	}
	return result.toString();
    }

    public String getStringFromTimeslot(Timeslot timeslot) {
	return timeslot.getId() + ". " + timeslot.getBeginTime() + "-" + timeslot.getEndTime();
    }

    public void addTimeslot() {
	jdbcTimeslotDao.create(createTimeslot());
    }

    public Timeslot createTimeslot() {
	System.out.print("Enter begin time: ");
	LocalTime beginTime = getTimeFromScanner();
	System.out.print("Enter end time: ");
	LocalTime endTime = getTimeFromScanner();

	return new Timeslot(beginTime, endTime);
    }

    public void printTimeslots() {
	System.out.println(getStringOfTimeslots(jdbcTimeslotDao.findAll()));
    }

    public void updateTimeslot() {
	Timeslot oldTimeslot = selectTimeslot();
	Timeslot newTimeslot = createTimeslot();
	newTimeslot.setId(oldTimeslot.getId());
	jdbcTimeslotDao.update(newTimeslot);
	System.out.println("Overwrite successful.");
    }

    public void deleteTimeslot() {
	jdbcTimeslotDao.delete(selectTimeslot().getId());
	System.out.println("Timeslot deleted successfully.");
    }

    public Timeslot selectTimeslot() {
	List<Timeslot> timeslots = jdbcTimeslotDao.findAll();
	Timeslot result = null;

	while (isNull(result)) {
	    System.out.println("Select timeslot: ");
	    System.out.print(getStringOfTimeslots(timeslots));
	    int choice = getIntFromScanner();
	    Optional<Timeslot> selectedTimeslot = jdbcTimeslotDao.findById(choice);
	    if (selectedTimeslot.isEmpty()) {
		System.out.println("No such timeslot.");
	    } else {
		result = selectedTimeslot.get();
		System.out.println("Success.");
	    }
	}
	return result;
    }
}
