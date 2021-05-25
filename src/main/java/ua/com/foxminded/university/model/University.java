package ua.com.foxminded.university.model;

import java.util.List;

import ua.com.foxminded.university.handlers.ClassroomsMenu;
import ua.com.foxminded.university.handlers.GroupsMenu;
import ua.com.foxminded.university.handlers.HolidaysMenu;
import ua.com.foxminded.university.handlers.LecturesMenu;
import ua.com.foxminded.university.handlers.StudentsMenu;
import ua.com.foxminded.university.handlers.TeachersMenu;

import static ua.com.foxminded.university.Menu.*;

public class University {

    private String name;
    private List<Teacher> teachers;
    private List<Classroom> classrooms;
    private List<Student> students;
    private List<Group> groups;
    private List<Lecture> lectures;
    private List<Holiday> holidays;
    private List<Subject> subjects;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<Teacher> getTeachers() {
	return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
	this.teachers = teachers;
    }

    public List<Classroom> getClassrooms() {
	return classrooms;
    }

    public void setClassrooms(List<Classroom> classrooms) {
	this.classrooms = classrooms;
    }

    public List<Group> getGroups() {
	return groups;
    }

    public void setGroups(List<Group> groups) {
	this.groups = groups;
    }

    public List<Lecture> getLectures() {
	return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
	this.lectures = lectures;
    }

    public List<Holiday> getHolidays() {
	return holidays;
    }

    public void setHolidays(List<Holiday> holidays) {
	this.holidays = holidays;
    }

    public List<Subject> getSubjects() {
	return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
	this.subjects = subjects;
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
	result.append(FORMAT_DIVIDER);
	result.append("University name: " + name + CR);
	result.append(FORMAT_DIVIDER);
	result.append("Teachers are:" + CR);
	result.append(TeachersMenu.getStringOfTeachers(teachers));
	result.append(FORMAT_DIVIDER);
	result.append("Students are:" + CR);
	result.append(StudentsMenu.getStringOfStudents(students));
	result.append(FORMAT_DIVIDER);
	result.append("Current student groups:" + CR);
	result.append(GroupsMenu.getStringOfGroups(groups));
	result.append(FORMAT_DIVIDER);
	result.append("Subjects available to students:");
	result.append(subjects + CR);
	result.append(FORMAT_DIVIDER);
	result.append("Classrooms list:" + CR);
	result.append(ClassroomsMenu.getStringOfClassrooms(classrooms));
	result.append(FORMAT_DIVIDER);
	result.append("Scheduled lectures:" + CR);
	result.append(LecturesMenu.getStringOfLectures(lectures));
	result.append(FORMAT_DIVIDER);
	result.append("Holidays are:" + CR);
	result.append(HolidaysMenu.getStringOfHolidays(holidays));
	result.append(FORMAT_DIVIDER);
	return result.toString();
    }
}
