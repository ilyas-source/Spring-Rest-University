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
	    System.out.print("Finished:" + finished + ", correct entry:" + correctEntry + CR);
	    if (result.size() > 0) {
		System.out.println("Assigned subjects:");
		System.out.print(SubjectsHandler.getStringOfSubjects(result));
	    }
	    System.out.print("Enter a new subject number to add to this teacher: " + CR);
	    System.out.print(SubjectsHandler.getStringOfSubjects(subjects) + CR);
	    correctEntry = false;
	    int choice = readNextInt() - 1;
	    if (choice <= subjects.size()) {
		Subject selected = subjects.get(choice);
		if (result.contains(selected)) {
		    System.out.println("Subject already assigned to the teacher.");
		} else {
		    correctEntry = true;
		    result.add(new Subject(selected.getName(), selected.getDescription()));
		    System.out.println("Success.");
		}
	    } else {
		System.out.println("No such subject.");
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
