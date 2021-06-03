package ua.com.foxminded.university.model;

public class Location {

    int id;
    private String building;
    private int floor;
    private int roomNumber;

    public Location(String building, int floor, int roomNumber) {
	this.building = building;
	this.floor = floor;
	this.roomNumber = roomNumber;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getBuilding() {
	return building;
    }

    public void setBuilding(String building) {
	this.building = building;
    }

    public int getFloor() {
	return floor;
    }

    public void setFloor(int floor) {
	this.floor = floor;
    }

    public int getRoomNumber() {
	return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
	this.roomNumber = roomNumber;
    }
}
