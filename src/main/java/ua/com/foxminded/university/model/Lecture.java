package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.util.List;

public class Lecture {

    private int id;
    private LocalDate date;
    private TimeRange time;
    private List<Group> groups;
    private Subject subject;
    private Teacher teacher;
    private Classroom classroom;

    public Lecture() {
    }

    public Lecture(LocalDate date, TimeRange time, List<Group> groups, Subject subject, Teacher teacher, Classroom classroom) {
	this.date = date;
	this.time = time;
	this.groups = groups;
	this.subject = subject;
	this.teacher = teacher;
	this.classroom = classroom;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
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

    public Classroom getClassroom() {
	return classroom;
    }

    public void setClassroom(Classroom classroom) {
	this.classroom = classroom;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((classroom == null) ? 0 : classroom.hashCode());
	result = prime * result + ((date == null) ? 0 : date.hashCode());
	result = prime * result + ((groups == null) ? 0 : groups.hashCode());
	result = prime * result + id;
	result = prime * result + ((subject == null) ? 0 : subject.hashCode());
	result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
	result = prime * result + ((time == null) ? 0 : time.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Lecture other = (Lecture) obj;
	if (classroom == null) {
	    if (other.classroom != null)
		return false;
	} else if (!classroom.equals(other.classroom))
	    return false;
	if (date == null) {
	    if (other.date != null)
		return false;
	} else if (!date.equals(other.date))
	    return false;
	if (groups == null) {
	    if (other.groups != null)
		return false;
	} else if (!groups.equals(other.groups))
	    return false;
	if (id != other.id)
	    return false;
	if (subject == null) {
	    if (other.subject != null)
		return false;
	} else if (!subject.equals(other.subject))
	    return false;
	if (teacher == null) {
	    if (other.teacher != null)
		return false;
	} else if (!teacher.equals(other.teacher))
	    return false;
	if (time == null) {
	    if (other.time != null)
		return false;
	} else if (!time.equals(other.time))
	    return false;
	return true;
    }
}
