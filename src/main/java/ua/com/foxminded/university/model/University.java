package ua.com.foxminded.university.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
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
}
