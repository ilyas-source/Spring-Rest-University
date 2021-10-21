package ua.com.foxminded.university.model;

public class Location {

    private int id;
    private String building;
    private int floor;
    private int roomNumber;

    public Location() {
    }

    public Location(String building, int floor, int roomNumber) {
        this.building = building;
        this.floor = floor;
        this.roomNumber = roomNumber;
    }

    public Location(int id, String building, int floor, int roomNumber) {
        this.id = id;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((building == null) ? 0 : building.hashCode());
        result = prime * result + floor;
        result = prime * result + id;
        result = prime * result + roomNumber;
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
        Location other = (Location) obj;
        if (building == null) {
            if (other.building != null)
                return false;
        } else if (!building.equals(other.building))
            return false;
        if (floor != other.floor)
            return false;
        if (id != other.id)
            return false;
        if (roomNumber != other.roomNumber)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return id + ": " + building + ", floor " + floor + ", room " + roomNumber;
    }
}
