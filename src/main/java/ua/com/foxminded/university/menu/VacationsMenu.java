package ua.com.foxminded.university.menu;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Vacation;

import java.util.Comparator;
import java.util.List;

import static ua.com.foxminded.university.Menu.CR;

@Component
public class VacationsMenu {

    public String getStringOfVacations(List<Vacation> vacations) {
        StringBuilder result = new StringBuilder();
        vacations.sort(Comparator.comparing(Vacation::getId));
        for (Vacation vacation : vacations) {
            result.append(vacation.getId()).append(". " + getStringFromVacation(vacation) + CR);
        }
        return result.toString();
    }

    public String getStringFromVacation(Vacation vacation) {
        StringBuilder result = new StringBuilder();
        result.append(vacation.getStartDate().toString() + "-" + vacation.getEndDate().toString());

        return result.toString();
    }




}
