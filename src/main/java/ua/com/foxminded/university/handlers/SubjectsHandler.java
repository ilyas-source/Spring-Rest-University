package ua.com.foxminded.university.handlers;

import java.util.List;

import ua.com.foxminded.university.model.Subject;

public class SubjectsHandler {

    public static void printOutSubjects(List<Subject> subjects) {
	for (Subject subject : subjects) {
	    System.out.println(subjects.indexOf(subject) + ". " + subject);
	}
    }
}
