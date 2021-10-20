package ua.com.foxminded.university.controller.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToLocalDate implements Converter<String, LocalDate> {

    private static final Logger logger = LoggerFactory.getLogger(StringToLocalDate.class);

    @Override
    public LocalDate convert(String source) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(source, formatter);
    }
}