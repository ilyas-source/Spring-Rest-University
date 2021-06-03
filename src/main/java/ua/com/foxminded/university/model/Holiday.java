package ua.com.foxminded.university.model;

import java.time.LocalDate;

public class Holiday {

    int id;
    private LocalDate date;
    private String name;

    public Holiday(LocalDate date, String name) {
	this.date = date;
	this.name = name;
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

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
}
