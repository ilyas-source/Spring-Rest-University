package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.CR;

import java.util.List;

import ua.com.foxminded.university.model.Holiday;

public class HolidaysMenu {

    public String getStringOfHolidays(List<Holiday> holidays) {
	StringBuilder result = new StringBuilder();

	for (Holiday holiday : holidays) {
	    result.append(holidays.indexOf(holiday) + 1).append(". " + getStringFromHoliday(holiday) + CR);
	}

	return result.toString();
    }

    public String getStringFromHoliday(Holiday holiday) {
	return holiday.getDate() + ": " + holiday.getName();
    }

}
