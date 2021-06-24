package ua.com.foxminded.university.model;

import java.time.LocalTime;

public class TimeRange {

    private int id;
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeRange() {
    }

    public TimeRange(LocalTime startTime, LocalTime endTime) {
	this.startTime = startTime;
	this.endTime = endTime;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public LocalTime getStartTime() {
	return startTime;
    }

    public void setStartTime(LocalTime startTime) {
	this.startTime = startTime;
    }

    public LocalTime getEndTime() {
	return endTime;
    }

    public void setEndTime(LocalTime endTime) {
	this.endTime = endTime;
    }
}
