package ua.com.foxminded.university.menu;

import java.util.Comparator;
import java.util.List;

import javax.sound.midi.Soundbank;

import static java.util.Objects.isNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcGroupDAO;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Group;
import static ua.com.foxminded.university.Menu.*;

@Component
public class GroupsMenu {

    @Autowired
    private JdbcGroupDAO jdbcGroupDAO;

    public String getStringOfGroups(List<Group> groups) {
	StringBuilder result = new StringBuilder();
	groups.sort(Comparator.comparing(Group::getId));
	for (Group group : groups) {
	    result.append(group.getId()).append(". " + group.getName()).append(CR);
	}
	return result.toString();
    }

    public void printGroups() {
	System.out.println(getStringOfGroups(jdbcGroupDAO.findAll()));
    }

    public Group addGroup() {
	System.out.print("Enter group name: ");
	String name = scanner.nextLine();
	return new Group(name);
    }

    public Group selectGroup() {
	List<Group> groups = jdbcGroupDAO.findAll();
	Group result = null;
	boolean correctEntry = false;
	while (!correctEntry) {
	    System.out.println("Select a group:");
	    System.out.print(getStringOfGroups(groups));
	    int choice = getIntFromScanner();
	    Group selected = jdbcGroupDAO.findById(choice).orElse(null);
	    if (isNull(selected)) {
		System.out.println("No such group, try again.");
	    } else {
		correctEntry = true;
		result = selected;
	    }
	}
	return result;
    }

//    public List<Group> selectGroups() {
//	List<Group> result = new ArrayList<>();
//	List<Group> groups = university.getGroups();
//	boolean finished = false;
//	boolean correctEntry = false;
//
//	while (!(finished && correctEntry)) {
//	    if (!result.isEmpty()) {
//		System.out.println("Assigned groups:");
//		System.out.print(getStringOfGroups(result));
//	    }
//	    System.out.println("Enter a new group number to add to this lecture: ");
//	    System.out.print(getStringOfGroups(groups));
//	    correctEntry = false;
//	    int choice = getIntFromScanner();
//	    if (choice <= groups.size()) {
//		Group selected = groups.get(choice - 1);
//		if (result.contains(selected)) {
//		    System.out.println("Group already added to the lecture.");
//		} else {
//		    correctEntry = true;
//		    result.add(new Group(selected.getName(), selected.getStudents()));
//		    System.out.println("Success.");
//		}
//	    } else {
//		System.out.println("No such group.");
//	    }
//	    System.out.print("Add another group? (y/n): ");
//	    String entry = scanner.nextLine().toLowerCase();
//	    if (!entry.equals("y")) {
//		finished = true;
//	    }
//	}
//	return result;
//    }
//
//    public void updateGroup() {
//	List<Group> groups = university.getGroups();
//
//	System.out.println("Select a group to update: ");
//	System.out.println(getStringOfGroups(groups));
//	int choice = getIntFromScanner();
//	if (choice > groups.size()) {
//	    System.out.println("No such group, returning...");
//	} else {
//	    groups.set(choice - 1, createGroup());
//	    System.out.println("Overwrite successful.");
//	}
//    }
//
    public void deleteGroup() {
	List<Group> groups = jdbcGroupDAO.findAll();

	System.out.println("Select a group to delete: ");
	System.out.println(getStringOfGroups(groups));
	int choice = getIntFromScanner();
	Group group = jdbcGroupDAO.findById(choice).orElse(null);

	if (isNull(group)) {
	    System.out.println("No such group, returning...");
	} else {
	    jdbcGroupDAO.delete(choice);
	    System.out.println("Group deleted successfully.");
	}
    }
}
