package ua.com.foxminded.university.handlers;

import java.util.ArrayList;
import java.util.List;

import static ua.com.foxminded.university.Menu.CR;
import static ua.com.foxminded.university.Menu.readNextInt;
import static ua.com.foxminded.university.Menu.scanner;

import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.University;

public class SubjectsHandler {

    public static String getStringOfSubjects(List<Subject> subjects) {
	StringBuilder result = new StringBuilder();
	for (Subject subject : subjects) {
	    result.append(subjects.indexOf(subject) + 1).append(". " + subject + CR);
	}
	return result.toString();
    }

    public static List<Subject> getSubjectsFromScanner(University university) {
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

    public static Subject getNewSubjectFromScanner(University university) {
	// TODO Auto-generated method stub
	return null;
    }

    public static void updateASubject(University university) {
	// TODO Auto-generated method stub

    }

    public static void deleteASubject(University university) {
	// TODO Auto-generated method stub

    }
}
