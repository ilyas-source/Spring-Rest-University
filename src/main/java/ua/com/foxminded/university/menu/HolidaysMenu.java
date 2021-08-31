package ua.com.foxminded.university.menu;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.service.HolidayService;

import java.util.Comparator;
import java.util.List;

import static ua.com.foxminded.university.Menu.CR;

@Component
public class HolidaysMenu {

    private HolidayService holidayService;

    public HolidaysMenu(HolidayService holidayService) {
        this.holidayService = holidayService;
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

    public void printHolidays() {
        System.out.println(getStringOfHolidays(holidayService.findAll()));
    }
}
