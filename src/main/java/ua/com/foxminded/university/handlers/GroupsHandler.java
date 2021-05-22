package ua.com.foxminded.university.handlers;

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
	    result.append(groups.indexOf(group) + ". " + group);
	}
	return result.toString();
    }
}
