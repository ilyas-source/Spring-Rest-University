package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.university.Menu;
import ua.com.foxminded.university.model.Vacation;

public class VacationsHandler {

    public static String getStringOfVacations(List<Vacation> vacations) {
	StringBuilder result = new StringBuilder();
	for (Vacation vacation : vacations) {
	    result.append(vacations.indexOf(vacation) + ". " + vacation + CR);
	}
	return result.toString();
    }

    public static Vacation getVacationFromScanner() {
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

    private static LocalDate getDateFromScanner() {
	scanner = new Scanner(System.in);
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Menu.DATE_FORMAT);
	LocalDate result = LocalDate.of(2000, 1, 1);

	Boolean correctEntry = false;
	while (!correctEntry) {
	    try {
		String line = scanner.nextLine();
		correctEntry = true;
		result = LocalDate.parse(line, formatter);
	    } catch (Exception e) {
		e.printStackTrace();
		correctEntry = false;
		System.exit(0);
	    }
	}
	return result;
    }
}
