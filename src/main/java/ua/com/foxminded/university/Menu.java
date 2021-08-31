package ua.com.foxminded.university;

import java.time.format.DateTimeFormatter;

//@Component
public class Menu {

    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final String CR = System.lineSeparator();
}
