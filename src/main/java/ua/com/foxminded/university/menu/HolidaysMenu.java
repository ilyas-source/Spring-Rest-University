package ua.com.foxminded.university.menu;

import static java.util.Objects.isNull;
import static ua.com.foxminded.university.Menu.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.Menu;
import ua.com.foxminded.university.dao.jdbc.JdbcHolidayDAO;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.model.Subject;

@Component
public class HolidaysMenu {

    @Autowired
    private JdbcHolidayDAO jdbcHolidayDAO;

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
	System.out.println(getStringOfHolidays(jdbcHolidayDAO.findAll()));
    }

    public void updateHoliday() {
	List<Holiday> holidays = jdbcHolidayDAO.findAll();

	System.out.println("Select a holiday to update: ");
	System.out.println(getStringOfHolidays(holidays));
	int choice = getIntFromScanner();
	Holiday holiday = jdbcHolidayDAO.findById(choice).orElse(null);

	if (isNull(holiday)) {
	    System.out.println("No such holiday, returning...");
	} else {
	    holiday = createHoliday();
	    holiday.setId(choice);
	    jdbcHolidayDAO.update(holiday);

	    System.out.println("Overwrite successful.");
	}
    }

    public void deleteHoliday() {
	List<Holiday> holidays = jdbcHolidayDAO.findAll();

	System.out.println("Select a holiday to delete: ");
	System.out.println(getStringOfHolidays(holidays));
	int choice = getIntFromScanner();
	Holiday holiday = jdbcHolidayDAO.findById(choice).orElse(null);

	if (isNull(holiday)) {
	    System.out.println("No such holiday, returning...");
	} else {
	    jdbcHolidayDAO.delete(choice);
	    System.out.println("Subject deleted successfully.");
	}
    }

    public void addHoliday() {
	jdbcHolidayDAO.addToDb(createHoliday());
    }

}
