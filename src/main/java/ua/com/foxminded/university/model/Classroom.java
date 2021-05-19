package ua.com.foxminded.university.model;

public class Classroom {

    private Location location;
    private String name;
    private int capacity;

    public Location getLocation() {
	return location;
    }

    public void setLocation(Location location) {
	this.location = location;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public int getCapacity() {
	return capacity;
    }

    public void setCapacity(int capacity) {
	this.capacity = capacity;
    }
}
