package ua.com.foxminded.university.handlers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.university.Menu;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.University;
import ua.com.foxminded.university.model.Vacation;

public class TeachersHandler {

    Scanner scanner = new Scanner(System.in);

    public void addTeacher(University university) {
	List<Teacher> teachers = university.getTeachers();
	List<Subject> subjects;
	List<Vacation> vacations;

	System.out.println("Entering address.");
	Address address = getAddressFromScanner();

	System.out.println("Assigning subjects.");
	subjects = getSubjectsFromScanner(university);

	System.out.print("First name: ");
	String firstName = scanner.nextLine();

	System.out.print("last name: ");
	String lastName = scanner.nextLine();

	System.out.println("Gender (M/F): ");
	Gender gender = getGenderFromScanner();

	System.out.println("Degree: (B)achelor/(M)aster/(D)octor: ");
	Degree degree = getDegreeFromScanner();

	System.out.println("Enter email:");
	String email = scanner.nextLine();

	System.out.println("Enter phone:");
	String phone = scanner.nextLine();

	System.out.println("Enter vacations: ");
	vacations = getVacationsFromScanner();

	Teacher teacher = new Teacher(firstName, lastName, gender, degree, subjects, email, phone, address, vacations);
	teachers.add(teacher);
	university.setTeachers(teachers);

	scanner.close();
    }

    private List<Subject> getSubjectsFromScanner(University university) {
	List<Subject> result = new ArrayList<>();
	List<Subject> subjects = university.getSubjects();
	Boolean finished = false;
	Boolean correctEntry = false;

	while (!(finished && correctEntry)) {
	    System.out.println("Assigned subjects, if any:");
	    SubjectsHandler.printOutSubjects(result);
	    System.out.println("Enter a new subject number to add to this teacher:");
	    SubjectsHandler.printOutSubjects(subjects);
	    correctEntry = false;
	    int choice = readIntFromScanner();
	    if (choice <= subjects.size()) {
		Subject selected = subjects.get(choice);
		if (result.contains(selected)) {
		    System.out.println("Subject already assigned to the teacher.");
		} else {
		    correctEntry = true;
		    Subject subject = new Subject(selected.getName(), selected.getDescription());
		    result.add(subject);
		    System.out.println("Successfully added subject " + subject.getName());
		}
	    } else {
		System.out.println("No such subject.");
	    }
	    if (!correctEntry) {
		System.out.print("Wrong input.");
	    }
	    System.out.print("Add another? (y/n): ");
	    scanner.nextLine();
	    String entry = scanner.nextLine().toLowerCase();
	    if (entry.equals("y")) {
		finished = false;
	    } else {
		finished = true;
	    }
	}
	return result;
    }

    private List<Vacation> getVacationsFromScanner() {
	LocalDate startDate;
	LocalDate endDate;
	Vacation vacation = new Vacation();
	List<Vacation> vacations = new ArrayList<Vacation>();
	boolean finished = false;

	while (!finished) {
	    System.out.print("Enter vacation start date (" + Menu.DATE_FORMAT + "): ");
	    startDate = getDateFromScanner();
	    vacation.setStartDate(startDate);

	    System.out.print("Enter vacation end date (" + Menu.DATE_FORMAT + "): ");
	    endDate = getDateFromScanner();
	    vacation.setEndDate(endDate);

	    vacations.add(vacation);

	    System.out.print("Add another vacation? (y/n): ");
	    String choice = scanner.nextLine().toLowerCase();
	    if (choice != "y") {
		finished = true;
	    }
	}
	return vacations;
    }

    private LocalDate getDateFromScanner() {
	LocalDate result = LocalDate.of(1900, 1, 1);
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Menu.DATE_FORMAT);

	try {
	    String line = scanner.nextLine();
	    result = LocalDate.parse(line, formatter);
	} catch (Exception e) {
	    System.err.println("Invalid date value");
	}
	return result;
    }

    private Address getAddressFromScanner() {
	// TODO Auto-generated method stub
	return null;
    }

    private Degree getDegreeFromScanner() {
	Boolean keepOn = true;
	String choice = "";

	while (keepOn) {
	    choice = scanner.nextLine().toLowerCase();
	    if (choice.equals("b") || choice.equals("m") || choice.equals("d")) {
		keepOn = false;
	    } else {
		System.out.println("Wrong input, try again:");
	    }
	}
	switch (choice) {
	case "b":
	    return Degree.BACHELOR;
	case "m":
	    return Degree.MASTER;
	default:
	    return Degree.DOCTOR;
	}
    }

    private Gender getGenderFromScanner() {
	Boolean keepOn = true;
	String choice = "";
	while (keepOn) {
	    choice = scanner.nextLine().toLowerCase();
	    if (choice.equals("m") || choice.equals("f")) {
		keepOn = false;
	    } else {
		System.out.println("Wrong input, try again:");
	    }
	}
	if (choice == "m") {
	    return Gender.MALE;
	} else {
	    return Gender.FEMALE;
	}
    }

    private int readIntFromScanner() {
	while (!scanner.hasNextInt()) {
	    scanner.next();
	}
	return scanner.nextInt();
    }
}
