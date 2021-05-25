package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ua.com.foxminded.university.Menu;
import ua.com.foxminded.university.model.Vacation;

public class VacationsMenu {

    public String getStringOfVacations(List<Vacation> vacations) {
	StringBuilder result = new StringBuilder();
	for (Vacation vacation : vacations) {
	    result.append(vacations.indexOf(vacation) + 1).append(". " + getStringOfVacation(vacation) + CR);
	}
	return result.toString();
    }

    public String getStringOfVacation(Vacation vacation) {
	return vacation.getStartDate().toString() + "-" + vacation.getEndDate().toString();
    }

    public List<Vacation> createVacations() {
	List<Vacation> vacations = new ArrayList<>();
	boolean finished = false;

	while (!finished) {
	    Vacation vacation = createVacation();
	    vacations.add(vacation);
	    System.out.print("Done. Add another vacation? (y/n): ");
	    String choice = scanner.nextLine().toLowerCase();
	    if (choice != "y") {
		finished = true;
	    }
	}
	return vacations;
    }

    public Vacation createVacation() {
	Vacation vacation = new Vacation();
	Boolean correctEntry = false;

	while (!correctEntry) {
	    System.out.print("Enter vacation start date (" + Menu.DATE_FORMAT + "): ");
	    LocalDate startDate = getDateFromScanner();
	    vacation.setStartDate(startDate);

	    System.out.print("Enter vacation end date (" + Menu.DATE_FORMAT + "): ");
	    LocalDate endDate = getDateFromScanner();
	    vacation.setEndDate(endDate);

	    if (endDate.isBefore(startDate)) {
		System.out.println("Wrong entry, try again.");
	    } else {
		correctEntry = true;
	    }
	}
	return vacation;
    }
}
