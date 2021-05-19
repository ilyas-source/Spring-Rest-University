package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.util.List;

public class Lecture {

    private LocalDate date;
    private TimeRange time;
    private List<Group> groups;
    private Subject subject;
    private Teacher teacher;
    private Classroom classroom;

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

    public Classroom getClassroom() {
	return classroom;
    }

    public void setClassroom(Classroom classroom) {
	this.classroom = classroom;
    }
}
