package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.*;

import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.University;

public class StudentsMenu {

    public static String getStringOfStudents(List<Student> students) {
	StringBuilder result = new StringBuilder();
	for (Student student : students) {
	    result.append(students.indexOf(student) + 1).append(". " + student + CR);
	}
	return result.toString();
    }

    public static List<Student> createStudent(University university) {
	List<Student> result = new ArrayList<>();
	List<Student> students = university.getStudents();
	Boolean finished = false;
	Boolean correctEntry = false;

	while (!(finished && correctEntry)) {
	    if (result.size() > 0) {
		System.out.println("Assigned students:");
		System.out.print(StudentsMenu.getStringOfStudents(result));
	    }
	    System.out.print("Enter a new student number to add: " + CR);
	    System.out.print(StudentsMenu.getStringOfStudents(students) + CR);
	    correctEntry = false;
	    int choice = getIntFromScanner() - 1;
	    if (choice <= students.size()) {
		Student selected = students.get(choice);
		if (result.contains(selected)) {
		    System.out.println("Student already assigned to the group.");
		} else {
		    correctEntry = true;
		    result.add(new Student(selected.getFirstName(), selected.getLastName(),
			    selected.getGender(), selected.getBirthDate(),
			    selected.getEntryYear(), selected.getEmail(),
			    selected.getPhoneNumber(), selected.getAddress()));
		    System.out.println("Success.");
		}
	    } else {
		System.out.println("No such student.");
	    }
	    System.out.print("Add another? (y/n): ");
	    String entry = scanner.nextLine().toLowerCase();
	    if (entry.equals("y")) {
		finished = false;
	    } else {
		finished = true;
	    }
	}
	return result;
    }
}
