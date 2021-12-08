package ua.com.foxminded.university.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class ClassroomDto {

    @NotEmpty(message = "{name.notempty}")
    private String name;
    @Positive(message = "{capacity.positive}")
    private int capacity;
    private int locationId;

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

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassroomDto that = (ClassroomDto) o;

        if (capacity != that.capacity) return false;
        if (locationId != that.locationId) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + capacity;
        result = 31 * result + locationId;
        return result;
    }

    @Override
    public String toString() {
        return "ClassroomDto{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                ", locationId=" + locationId +
                '}';
    }
}
