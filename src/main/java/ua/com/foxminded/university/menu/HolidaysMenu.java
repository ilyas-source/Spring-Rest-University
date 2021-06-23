package ua.com.foxminded.university.menu;

import static java.util.Objects.isNull;
import static ua.com.foxminded.university.Menu.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.Menu;
import ua.com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import ua.com.foxminded.university.model.Holiday;

@Component
public class HolidaysMenu {

    private JdbcHolidayDao jdbcHolidayDao;

    public HolidaysMenu(JdbcHolidayDao jdbcHolidayDao) {
	this.jdbcHolidayDao = jdbcHolidayDao;
    }

    public String getStringOfHolidays(List<Holiday> holidays) {
	StringBuilder result = new StringBuilder();

	holidays.sort(Comparator.comparing(Holiday::getId));
	for (Holiday holiday : holidays) {
	    result.append(holiday.getId()).append(". " + getStringFromHoliday(holiday) + CR);
	}
	return result.toString();
    }

    public String getStringFromHoliday(Holiday holiday) {
	return holiday.getDate() + ": " + holiday.getName();
    }

    public Holiday createHoliday() {
	System.out.print("Enter date: ");
	LocalDate date = Menu.getDateFromScanner();
	System.out.print("Enter description: ");
	String name = scanner.nextLine();

	return new Holiday(date, name);
    }

    public void printHolidays() {
	System.out.println(getStringOfHolidays(jdbcHolidayDao.findAll()));
    }

    public Holiday selectHoliday() {
	List<Holiday> holidays = jdbcHolidayDao.findAll();
	Holiday result = null;

	while (isNull(result)) {
	    System.out.println("Select holiday: ");
	    System.out.print(getStringOfHolidays(holidays));
	    int choice = getIntFromScanner();
	    Holiday selected = jdbcHolidayDao.findById(choice).orElse(null);
	    if (isNull(selected)) {
		System.out.println("No such holiday.");
	    } else {
		result = selected;
		System.out.println("Success.");
	    }
	}
	return result;
    }

    public void updateHoliday() {
	Holiday oldHoliday = selectHoliday();
	Holiday newHoliday = createHoliday();
	newHoliday.setId(oldHoliday.getId());
	jdbcHolidayDao.update(newHoliday);
	System.out.println("Overwrite successful.");
    }

    public void deleteHoliday() {
	jdbcHolidayDao.delete(selectHoliday().getId());
	System.out.println("Holiday deleted successfully.");
    }

    public void addHoliday() {
	jdbcHolidayDao.create(createHoliday());
    }

}
