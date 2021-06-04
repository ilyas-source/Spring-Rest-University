package ua.com.foxminded.university.model;

import java.time.LocalDate;

public class Vacation {

    int id;
    private LocalDate startDate;
    private LocalDate endDate;

    public Vacation() {
    }

    public Vacation(LocalDate startDate, LocalDate endDate) {
	this.startDate = startDate;
	this.endDate = endDate;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
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
}
