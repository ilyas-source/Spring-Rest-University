package ua.com.foxminded.university.menu;

import java.util.ArrayList;
import java.util.List;

import static ua.com.foxminded.university.Menu.*;

import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.University;

public class SubjectsMenu {

    private University university;

    public SubjectsMenu(University university) {
	this.university = university;
    }

    public String getStringOfSubjects(List<Subject> subjects) {
	StringBuilder result = new StringBuilder();
	for (Subject subject : subjects) {
	    result.append(subjects.indexOf(subject) + 1).append(". " + getStringFromSubject(subject) + CR);
	}
	return result.toString();
    }

    public String getStringFromSubject(Subject subject) {
	return subject.getName() + ": " + subject.getDescription();
    }

    public Subject selectSubject() {
	List<Subject> subjects = university.getSubjects();
	Boolean correctEntry = false;
	Subject result = null;

	while (!correctEntry) {
	    System.out.println("Select subject: ");
	    System.out.print(getStringOfSubjects(subjects));
	    int choice = getIntFromScanner() - 1;
	    if (choice > subjects.size()) {
		System.out.println("No such object.");
	    } else {
		result = subjects.get(choice);
		System.out.println("Success.");
		correctEntry = true;
	    }
	}
	return result;
    }

    public List<Subject> selectSubjects() {
	List<Subject> result = new ArrayList<>();
	List<Subject> subjects = university.getSubjects();
	Boolean finished = false;
	Boolean correctEntry = false;

	while (!(finished && correctEntry)) {
	    if (!result.isEmpty()) {
		System.out.println("Assigned subjects:");
		System.out.print(getStringOfSubjects(result));
	    }
	    System.out.print("Enter a new subject number to add to this teacher: " + CR);
	    System.out.print(getStringOfSubjects(subjects) + CR);
	    correctEntry = false;
	    int choice = getIntFromScanner() - 1;
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
	    if (!entry.equals("y")) {
		finished = true;
	    }
	}
	return result;
    }

    public Subject createSubject() {
	System.out.print("Enter subject name: ");
	String name = scanner.nextLine();
	System.out.print("Enter description: ");
	String description = scanner.nextLine();

	return new Subject(name, description);
    }

    public void updateSubject() {
	List<Subject> subjects = university.getSubjects();

	System.out.println("Select a subject to update: ");
	System.out.println(getStringOfSubjects(subjects));
	int choice = getIntFromScanner() - 1;
	if (choice > subjects.size()) {
	    System.out.println("No such subject, returning...");
	} else {
	    subjects.set(choice, createSubject());
	    System.out.println("Overwrite successful.");
	}
    }

    public void deleteSubject() {
	List<Subject> subjects = university.getSubjects();

	System.out.println("Select a subject to delete: ");
	System.out.println(getStringOfSubjects(subjects));
	int choice = getIntFromScanner() - 1;
	if (choice > subjects.size()) {
	    System.out.println("No such subject, returning...");
	} else {
	    subjects.remove(choice);
	    System.out.println("Subject deleted successfully.");
	}
    }

}
