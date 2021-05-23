package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.CR;

import java.util.List;

import ua.com.foxminded.university.model.Holiday;

public class HolidaysHandler {

    public static String getStringOfHolidays(List<Holiday> holidays) {
	StringBuilder result = new StringBuilder();

	for (Holiday holiday : holidays) {
	    result.append(holidays.indexOf(holiday) + 1).append(". " + holiday + CR);
	}
	return result.toString();
    }

}
