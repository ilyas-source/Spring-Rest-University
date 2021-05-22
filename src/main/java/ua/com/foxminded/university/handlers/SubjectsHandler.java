package ua.com.foxminded.university.handlers;

import java.util.List;

import static ua.com.foxminded.university.Menu.CR;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.University;

public class SubjectsHandler {

    public static String getStringOfSubjects(List<Subject> subjects) {
	StringBuilder result = new StringBuilder();
	for (Subject subject : subjects) {
	    result.append(subjects.indexOf(subject) + ". " + subject + CR);
	}
	return result.toString();
    }

    public static Subject getSubjectFromScanner(University university) {
	// TODO Auto-generated method stub
	return null;
    }

    public static void updateASubject(University university) {
	// TODO Auto-generated method stub

    }

    public static void deleteASubject(University university) {
	// TODO Auto-generated method stub

    }
}
