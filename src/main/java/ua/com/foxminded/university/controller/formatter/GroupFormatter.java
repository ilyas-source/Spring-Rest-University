package ua.com.foxminded.university.controller.formatter;

import org.springframework.format.Formatter;
import ua.com.foxminded.university.model.Group;

import java.text.ParseException;
import java.util.Locale;

public class GroupFormatter implements Formatter<Group> {


    @Override
    public Group parse(String text, Locale locale) throws ParseException {
        Group group=new Group();
        if (text != null) {
            String[] parts = text.split(",");
            group.setId(Integer.parseInt(parts[0]));
            if(parts.length>1) {
                group.setName(parts[1]);
            }
        }
        return group;
    }

    @Override
    public String print(Group group, Locale locale) {
        return group.toString();
    }
}
