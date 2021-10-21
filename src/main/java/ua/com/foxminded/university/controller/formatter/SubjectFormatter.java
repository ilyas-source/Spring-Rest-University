package ua.com.foxminded.university.controller.formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;
import ua.com.foxminded.university.model.Subject;

import java.text.ParseException;
import java.util.Locale;

public class SubjectFormatter implements Formatter<Subject> {

    private static final Logger logger = LoggerFactory.getLogger(SubjectFormatter.class);

    @Override
    public Subject parse(String text, Locale locale) throws ParseException {
        logger.debug("Parsing {}", text);
        Subject subject = new Subject();
        String[] parts = text.split(":");
        subject.setId(Integer.parseInt(parts[0]));
        if (parts.length > 1) {
            subject.setName(parts[1]);
        }
        logger.debug("Parsing complete: {}", subject);
        return subject;
    }

    @Override
    public String print(Subject subject, Locale locale) {
        return subject.toString();
    }
}



