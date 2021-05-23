package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.university.Menu;
import ua.com.foxminded.university.model.Vacation;

public class VacationsHandler {

    public static String getStringOfVacations(List<Vacation> vacations) {
	StringBuilder result = new StringBuilder();
	for (Vacation vacation : vacations) {
	    result.append(vacations.indexOf(vacation) + 1).append(". " + vacation + CR);
	}
	return result.toString();
    }

    public static List<Vacation> getVacationsFromScanner() {

	List<Vacation> vacations = new ArrayList<>();
	boolean finished = false;

	while (!finished) {
	    Vacation vacation = VacationsHandler.getNewVacationFromScanner();
	    vacations.add(vacation);
	    System.out.print("Done. Add another vacation? (y/n): ");
	    String choice = scanner.nextLine().toLowerCase();
	    if (choice != "y") {
		finished = true;
	    }
	}
	return vacations;
    }

    public static Vacation getNewVacationFromScanner() {
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
