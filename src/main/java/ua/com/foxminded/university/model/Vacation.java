package ua.com.foxminded.university.model;

import java.time.LocalDate;

public class Vacation {

    private int id;
    private Teacher teacher;
    private LocalDate startDate;
    private LocalDate endDate;

    public Vacation() {
    }

    public Vacation(Teacher teacher, LocalDate startDate, LocalDate endDate) {
	this.teacher = teacher;
	this.startDate = startDate;
	this.endDate = endDate;
    }

    public Vacation(LocalDate startDate, LocalDate endDate) {
	this.startDate = startDate;
	this.endDate = endDate;
    }

    public Vacation(int id, Teacher teacher, LocalDate startDate, LocalDate endDate) {
	this.id = id;
	this.teacher = teacher;
	this.startDate = startDate;
	this.endDate = endDate;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public Teacher getTeacher() {
	return teacher;
    }

    public void setTeacher(Teacher teacher) {
	this.teacher = teacher;
    }

    public LocalDate getStartDate() {
	return startDate;
    }

    public void setStartDate(LocalDate startDate) {
	this.startDate = startDate;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
	result = prime * result + id;
	result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
	result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
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
	Vacation other = (Vacation) obj;
	if (endDate == null) {
	    if (other.endDate != null)
		return false;
	} else if (!endDate.equals(other.endDate))
	    return false;
	if (id != other.id)
	    return false;
	if (startDate == null) {
	    if (other.startDate != null)
		return false;
	} else if (!startDate.equals(other.startDate))
	    return false;
	if (teacher == null) {
	    if (other.teacher != null)
		return false;
	} else if (!teacher.equals(other.teacher))
	    return false;
	return true;
    }
}
