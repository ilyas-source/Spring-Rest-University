package ua.com.foxminded.university.handlers;

import java.util.List;

import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.University;

import static ua.com.foxminded.university.Menu.*;

public class GroupsHandler {

    public static String getStringOfGroups(List<Group> groups) {
	StringBuilder result = new StringBuilder();
	for (Group group : groups) {
	    result.append(groups.indexOf(group) + 1).append(". " + group);
	}
	return result.toString();
    }

    public static Group getNewGroupFromScanner(University university) {
	System.out.print("Enter group name: ");
	String name = scanner.nextLine();
	System.out.println("Assigning students to this group. ");
	List<Student> students = StudentsHandler.getStudentsFromScanner(university);

	return new Group(name, students);
    }

    public static void updateAGroup(University university) {
	// TODO Auto-generated method stub

    }

    public static void deleteAGroup(University university) {
	// TODO Auto-generated method stub

    }
}
