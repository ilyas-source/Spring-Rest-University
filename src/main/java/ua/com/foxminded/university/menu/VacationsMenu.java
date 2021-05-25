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
	Boolean correctEntry = false;
	LocalDate startDate = null;
	LocalDate endDate = null;

	while (!correctEntry) {
	    System.out.print("Enter vacation start date: ");
	    startDate = Menu.getDateFromScanner();

	    System.out.print("Enter vacation end date: ");
	    endDate = getDateFromScanner();

	    if (endDate.isBefore(startDate)) {
		System.out.println("Wrong entry, try again.");
	    } else {
		correctEntry = true;
	    }
	}
	return new Vacation(startDate, endDate);
    }
}
