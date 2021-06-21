package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

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
	return vacation.getStartDate().toString() + "-" + vacation.getEndDate().toString();
    }

    public List<Vacation> createVacations() {
	List<Vacation> vacations = new ArrayList<>();
	boolean finished = false;

	System.out.println("Entering vacations.");

	while (!finished) {
	    Vacation vacation = createVacation();
	    vacations.add(vacation);
	    System.out.print("Done. Add another vacation? (y/n): ");
	    String entry = scanner.nextLine().toLowerCase();
	    if (!entry.equals("y")) {
		finished = true;
	    }
	}
	return vacations;
    }

    public Vacation createVacation() {
	Vacation result = null;

	while (isNull(result)) {
	    System.out.print("Enter vacation start date: ");
	    LocalDate startDate = getDateFromScanner();

	    System.out.print("Enter vacation end date: ");
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
