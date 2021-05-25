package ua.com.foxminded.university.handlers;

import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.University;

import static ua.com.foxminded.university.Menu.*;

public class GroupsMenu {

    public static String getStringOfGroups(List<Group> groups) {
	StringBuilder result = new StringBuilder();
	for (Group group : groups) {
	    result.append(groups.indexOf(group) + 1).append(". " + group);
	}
	return result.toString();
    }

    public static Group createGroup(University university) {
	System.out.print("Enter group name: ");
	String name = scanner.nextLine();
	System.out.println("Assigning students to this group. ");
	List<Student> students = StudentsMenu.createStudent(university);

	return new Group(name, students);
    }

    public static List<Group> selectGroups(University university) {
	List<Group> result = new ArrayList<>();
	List<Group> groups = university.getGroups();
	Boolean finished = false;
	Boolean correctEntry = false;

	while (!(finished && correctEntry)) {
	    if (result.size() > 0) {
		System.out.println("Assigned groups:");
		System.out.print(getStringOfGroups(result));
	    }
	    System.out.print("Enter a new group number to add to this lecture: " + CR);
	    System.out.print(getStringOfGroups(groups) + CR);
	    correctEntry = false;
	    int choice = getIntFromScanner() - 1;
	    if (choice <= groups.size()) {
		Group selected = groups.get(choice);
		if (result.contains(selected)) {
		    System.out.println("Group already added to the lecture.");
		} else {
		    correctEntry = true;
		    result.add(new Group(selected.getName(), selected.getStudents()));
		    System.out.println("Success.");
		}
	    } else {
		System.out.println("No such group.");
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

    public static void updateGroup(University university) {
	List<Group> groups = university.getGroups();

	System.out.println("Select a group to update: ");
	System.out.println(getStringOfGroups(groups));
	int choice = getIntFromScanner() - 1;
	if (choice > groups.size()) {
	    System.out.println("No such group, returning...");
	} else {
	    groups.set(choice, createGroup(university));
	    System.out.println("Overwrite successful.");
	}
    }

    public static void deleteGroup(University university) {
	List<Group> groups = university.getGroups();

	System.out.println("Select a group to delete: ");
	System.out.println(getStringOfGroups(groups));
	int choice = getIntFromScanner() - 1;
	if (choice > groups.size()) {
	    System.out.println("No such group, returning...");
	} else {
	    groups.remove(choice);
	    System.out.println("Group deleted successfully.");
	}
    }

}
