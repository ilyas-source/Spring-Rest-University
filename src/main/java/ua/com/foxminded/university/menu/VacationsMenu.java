package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

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

    public List<Vacation> createVacations() {
	List<Vacation> result = new ArrayList<>();
	boolean finished = false;
	while (!finished) {
	    System.out.println("Add new vacation for teacher? (y/n):");
	    String entry = scanner.nextLine().toLowerCase();
	    if (!entry.equals("y")) {
		finished = true;
	    } else {
		Vacation vacation = createVacation();
		result.add(new Vacation(vacation.getStartDate(), vacation.getEndDate()));
	    }
	}
	return result;
    }

    private Vacation createVacation() {
	Vacation result = null;

	while (isNull(result)) {
	    System.out.print("Start date: ");
	    LocalDate startDate = getDateFromScanner();

	    System.out.print("End date: ");
	    LocalDate endDate = getDateFromScanner();

	    if (endDate.isBefore(startDate)) {
		System.out.println("Wrong entry, try again.");
	    } else {
		result = new Vacation(startDate, endDate);
	    }
	}
	return result;
    }
}
