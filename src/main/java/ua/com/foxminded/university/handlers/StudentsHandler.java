package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.CR;

import java.util.List;

import ua.com.foxminded.university.model.Student;

public class StudentsHandler {

    public static String getStringOfStudents(List<Student> students) {
	StringBuilder result = new StringBuilder();
	for (Student student : students) {
	    result.append(students.indexOf(student) + 1).append(". " + student + CR);
	}
	return result.toString();
    }

}
