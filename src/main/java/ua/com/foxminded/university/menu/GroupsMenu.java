package ua.com.foxminded.university.menu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import static ua.com.foxminded.university.Menu.*;

@Component
public class GroupsMenu {

    // private JdbcGroupDao jdbcGroupDao;

//    public GroupsMenu(JdbcGroupDao jdbcGroupDao) {
//	this.jdbcGroupDao = jdbcGroupDao;
//    }

    private GroupService groupService;

    public GroupsMenu(GroupService groupService) {
	this.groupService = groupService;
    }

    public String getStringOfGroups(List<Group> groups) {
	StringBuilder result = new StringBuilder();
	groups.sort(Comparator.comparing(Group::getId));
	for (Group group : groups) {
	    result.append(group.getId()).append(". " + group.getName()).append(CR);
	}
	return result.toString();
    }

    public void printGroups() {
	System.out.println(getStringOfGroups(groupService.findAll()));
    }

    public void addGroup() {
//	jdbcGroupDao.create(createGroup());
	groupService.create(createGroup());
    }

    public Group createGroup() {
	System.out.print("Enter group name: ");
	String name = scanner.nextLine();
	return new Group(name);
    }

    public Group selectGroup() {
	// List<Group> groups = jdbcGroupDao.findAll();
	List<Group> groups = groupService.findAll();
	Group result = null;
	boolean correctEntry = false;
	while (!correctEntry) {
	    System.out.println("Select a group:");
	    System.out.print(getStringOfGroups(groups));
	    int choice = getIntFromScanner();
	    Optional<Group> selectedGroup = groupService.findById(choice);
	    if (selectedGroup.isEmpty()) {
		System.out.println("No such group, try again.");
	    } else {
		correctEntry = true;
		result = selectedGroup.get();
	    }
	}
	return result;
    }

    public List<Group> selectGroups() {
	List<Group> result = new ArrayList<>();
	boolean finished = false;
	boolean correctEntry = false;

	while (!(finished && correctEntry)) {
	    if (!result.isEmpty()) {
		System.out.println("Assigned groups:");
		System.out.print(getStringOfGroups(result));
	    }
	    Group selected = selectGroup();
	    if (result.contains(selected)) {
		System.out.println("Group already added to the lecture.");
	    } else {
		correctEntry = true;
		result.add(new Group(selected.getId(), selected.getName()));
		System.out.println("Success.");
	    }

	    System.out.print("Add another group? (y/n): ");
	    String entry = scanner.nextLine().toLowerCase();
	    if (!entry.equals("y")) {
		finished = true;
	    }
	}
	return result;
    }

    public void updateGroup() {
	Group oldGroup = selectGroup();
	Group newGroup = createGroup();
	newGroup.setId(oldGroup.getId());
	groupService.update(newGroup);
	System.out.println("Overwrite successful.");
    }

    public void deleteGroup() {
	groupService.delete(selectGroup().getId());
	System.out.println("Group deleted successfully.");
    }
}
