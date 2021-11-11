package ua.com.foxminded.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        System.setProperty("server.servlet.context-path", "/university");
        SpringApplication.run(Main.class, args);
    }
}
