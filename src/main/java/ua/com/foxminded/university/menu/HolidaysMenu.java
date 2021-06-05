package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.Menu;
import ua.com.foxminded.university.dao.jdbc.JdbcHolidayDAO;
import ua.com.foxminded.university.model.Holiday;

@Component
public class HolidaysMenu {

    @Autowired
    private JdbcHolidayDAO jdbcHolidayDAO;

    public String getStringOfHolidays(List<Holiday> holidays) {
	StringBuilder result = new StringBuilder();

	holidays.sort(Comparator.comparing(Holiday::getId));
	for (Holiday holiday : holidays) {
	    result.append(holidays.indexOf(holiday) + 1).append(". " + getStringFromHoliday(holiday) + CR);
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

//    public void updateHoliday() {
//	List<Holiday> holidays = university.getHolidays();
//
//	System.out.println("Select a holiday to update: ");
//	System.out.println(getStringOfHolidays(holidays));
//	int choice = getIntFromScanner();
//	if (choice > holidays.size()) {
//	    System.out.println("No such holiday, returning...");
//	} else {
//	    holidays.set(choice - 1, createHoliday());
//	    System.out.println("Overwrite successful.");
//	}
//    }
//
//    public void deleteHoliday() {
//	List<Holiday> holidays = university.getHolidays();
//
//	System.out.println("Select a holiday to update: ");
//	System.out.println(getStringOfHolidays(holidays));
//	int choice = getIntFromScanner();
//	if (choice > holidays.size()) {
//	    System.out.println("No such holiday, returning...");
//	} else {
//	    holidays.remove(choice - 1);
//	    System.out.println("Holiday deleted successfully.");
//	}
//    }

}
