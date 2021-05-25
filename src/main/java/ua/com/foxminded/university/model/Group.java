package ua.com.foxminded.university.model;

import java.util.List;

import ua.com.foxminded.university.handlers.StudentsMenu;

import static ua.com.foxminded.university.Menu.*;

public class Group {

    private String name;
    private List<Student> students;

    public Group(String name, List<Student> students) {
	super();
	this.name = name;
	this.students = students;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<Student> getStudents() {
	return students;
    }

    public void setStudents(List<Student> students) {
	this.students = students;
    }

    @Override
    public String toString() {
	StringBuilder result = new StringBuilder();
	result.append("Group " + name + ":" + CR);
	for (Student student : students) {
	    result.append(student.getFirstName() + " " + student.getLastName() + CR);
	}
	return result.toString();
    }
}
