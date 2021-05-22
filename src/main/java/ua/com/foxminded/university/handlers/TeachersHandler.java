package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.*;

import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.university.Menu;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.University;
import ua.com.foxminded.university.model.Vacation;

public class TeachersHandler {

    public void addTeacher(University university) {
	List<Teacher> teachers = university.getTeachers();
	List<Subject> subjects;
	List<Vacation> vacations;

	scanner.nextLine();
	System.out.print("Enter first name: ");
	String firstName = scanner.nextLine();

	System.out.print("Enter last name: ");
	String lastName = scanner.nextLine();

	System.out.println("Gender (M/F): ");
	Gender gender = GenderHandler.getGenderFromScanner();

	System.out.println("Degree: (B)achelor/(M)aster/(D)octor: ");
	Degree degree = getDegreeFromScanner();

	System.out.println("Email:");
	String email = scanner.nextLine();

	System.out.println("Phone:");
	String phone = scanner.nextLine();

	System.out.println("Entering address.");
	Address address = AddressHandler.getAddressFromScanner();

	System.out.println("Assigning subjects.");
	subjects = getSubjectsFromScanner(university);

	System.out.println("Entering vacations.");
	vacations = getVacationsFromScanner();

	Teacher teacher = new Teacher(firstName, lastName, gender, degree, subjects, email, phone, address, vacations);

	System.out.println("Adding teacher with the following parameters:");
	System.out.println(teacher);

	teachers.add(teacher);
	university.setTeachers(teachers);
    }

    private List<Subject> getSubjectsFromScanner(University university) {
	List<Subject> result = new ArrayList<>();
	List<Subject> subjects = university.getSubjects();
	Boolean finished = false;
	Boolean correctEntry = false;

	while (!(finished && correctEntry)) {
	    if (result.size() > 0) {
		System.out.println("Assigned subjects:");
		System.out.print(SubjectsHandler.getStringOfSubjects(result));
	    }
	    System.out.println("Enter a new subject number to add to this teacher:");
	    System.out.print(SubjectsHandler.getStringOfSubjects(subjects));
	    correctEntry = false;
	    int choice = readNextInt();
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

	List<Vacation> vacations = new ArrayList<Vacation>();
	boolean finished = false;

	while (!finished) {
	    Vacation vacation = VacationsHandler.getVacationFromScanner();
	    vacations.add(vacation);
	    System.out.print("Done. Add another vacation? (y/n): ");
	    String choice = scanner.nextLine().toLowerCase();
	    if (choice != "y") {
		finished = true;
	    }
	}
	return vacations;
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

    public static String getStringOfTeachers(List<Teacher> teachers) {
	StringBuilder result = new StringBuilder();
	for (Teacher teacher : teachers) {
	    result.append(teachers.indexOf(teacher) + ". " + teacher + CR);
	}
	return result.toString();
    }

    public static void updateATeacher(List<Teacher> teachers) {
	System.out.println("Select a teacher to edit: ");
	System.out.println(getStringOfTeachers(teachers));
	int choice = Menu.readNextInt();
	if (choice > teachers.size()) {
	    System.out.println("No such teacher, returning...");
	} else {
	    Teacher teacher = teachers.get(choice);

	}
    }

}
