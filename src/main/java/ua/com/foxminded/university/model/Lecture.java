package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.university.handlers.GroupsHandler;

import static ua.com.foxminded.university.Menu.*;

public class Lecture {

    private LocalDate date;
    private TimeRange time;
    private List<Group> groups;
    private Subject subject;
    private Teacher teacher;
    private ClassRoom classroom;

    public Lecture(LocalDate date, TimeRange time, List<Group> groups, Subject subject, Teacher teacher, ClassRoom classroom) {
	super();
	this.date = date;
	this.time = time;
	this.groups = groups;
	this.subject = subject;
	this.teacher = teacher;
	this.classroom = classroom;
    }

    public LocalDate getDate() {
	return date;
    }

    public void setDate(LocalDate date) {
	this.date = date;
    }

    public TimeRange getTime() {
	return time;
    }

    public void setTime(TimeRange time) {
	this.time = time;
    }

    public List<Group> getGroups() {
	return groups;
    }

    public void setGroups(List<Group> groups) {
	this.groups = groups;
    }

    public Subject getSubject() {
	return subject;
    }

    public void setSubject(Subject subject) {
	this.subject = subject;
    }

    public Teacher getTeacher() {
	return teacher;
    }

    public void setTeacher(Teacher teacher) {
	this.teacher = teacher;
    }

    public ClassRoom getClassroom() {
	return classroom;
    }

    public void setClassroom(ClassRoom classroom) {
	this.classroom = classroom;
    }

    @Override
    public String toString() {
	StringBuilder result = new StringBuilder();

	result.append("Lecture on " + subject.getName() + " will take place on " + date + ", from " + time.getStartTime() + " to "
		+ time.getEndTime() + "." + CR);
	result.append("Read by " + teacher.getFirstName() + " " + teacher.getLastName() + " in " + classroom.getName()
		+ "." + CR);
	result.append("Groups to attend:" + CR + GroupsHandler.getStringOfGroups(groups));
	return result.toString();
    }
}
