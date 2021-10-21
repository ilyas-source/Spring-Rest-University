package ua.com.foxminded.university.controller.formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;
import ua.com.foxminded.university.model.Group;

import java.text.ParseException;
import java.util.Locale;

public class GroupFormatter implements Formatter<Group> {

    private static final Logger logger = LoggerFactory.getLogger(GroupFormatter.class);


    @Override
    public Group parse(String text, Locale locale) throws ParseException {
        logger.debug("Parsing {}", text);
        Group group = new Group();
        String[] parts = text.split(":");
        group.setId(Integer.parseInt(parts[0]));
        if (parts.length > 1) {
            group.setName(parts[1]);
        }
        logger.debug("Parsing complete: {}", group);
        return group;
    }

    @Override
    public String print(Group group, Locale locale) {
        return group.toString();
    }
}
