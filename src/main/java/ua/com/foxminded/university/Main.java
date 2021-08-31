package ua.com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.config.ApplicationConfig;

public class Main {

    public static final String CR = System.lineSeparator();

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        context.close();
    }
}
