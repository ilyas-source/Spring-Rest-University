package ua.com.foxminded.university.menu;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import java.util.Comparator;
import java.util.List;

import static ua.com.foxminded.university.Main.CR;

@Component
public class GroupsMenu {

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
}
