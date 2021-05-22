package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.CR;

import java.util.List;

import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.University;

public class GroupsHandler {

    public void addGroup(University university) {
	// TODO Auto-generated method stub
    }

    public static String getStringOfGroups(List<Group> groups) {
	StringBuilder result = new StringBuilder();
	for (Group group : groups) {
	    result.append(groups.indexOf(group) + ". " + group + CR);
	}
	return result.toString();
    }
}
