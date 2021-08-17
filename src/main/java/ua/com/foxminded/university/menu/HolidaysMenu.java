package ua.com.foxminded.university.menu;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.Menu;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.service.HolidayService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static ua.com.foxminded.university.Menu.*;

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

    public Holiday createHoliday() {
        System.out.print("Enter date: ");
        LocalDate date = Menu.getDateFromScanner();
        System.out.print("Enter description: ");
        String name = scanner.nextLine();

        return new Holiday(date, name);
    }

    public void printHolidays() {
        System.out.println(getStringOfHolidays(holidayService.findAll()));
    }

    public Holiday selectHoliday() {
        List<Holiday> holidays = holidayService.findAll();
        Holiday result = null;

        while (isNull(result)) {
            System.out.println("Select holiday: ");
            System.out.print(getStringOfHolidays(holidays));
            int choice = getIntFromScanner();
            Optional<Holiday> selectedHoliday = holidayService.findById(choice);
            if (selectedHoliday.isEmpty()) {
                System.out.println("No such holiday.");
            } else {
                result = selectedHoliday.get();
                System.out.println("Success.");
            }
        }
        return result;
    }

    public void updateHoliday() {
        Holiday oldHoliday = selectHoliday();
        Holiday newHoliday = createHoliday();
        newHoliday.setId(oldHoliday.getId());
        holidayService.update(newHoliday);
        System.out.println("Overwrite successful.");
    }

    public void deleteHoliday() {
        holidayService.delete(selectHoliday().getId());
        System.out.println("Holiday deleted successfully.");
    }

    public void addHoliday() {
        holidayService.create(createHoliday());
    }

}
