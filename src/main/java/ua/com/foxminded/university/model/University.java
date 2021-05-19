package ua.com.foxminded.university.model;

import java.util.List;

public class University {

    private String name;
    private List<Teacher> teachers;
    private List<Classroom> classrooms;
    private List<Group> groups;
    private List<Lecture> lectures;
    private List<Holiday> holidays;

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
}
