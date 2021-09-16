package ua.com.foxminded.university.menu;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Location;

import java.util.Comparator;
import java.util.List;

import static ua.com.foxminded.university.Main.*;

@Component
public class LocationsMenu {

    public String getStringOfLocations(List<Location> locations) {
        StringBuilder result = new StringBuilder();
        locations.sort(Comparator.comparing(Location::getId));

        for (Location location : locations) {
            result.append(getStringFromLocation(location) + CR);
        }
        return result.toString();
    }

    public String getStringFromLocation(Location location) {
        return location.getBuilding() + ", floor #" + location.getFloor() + ", room " + location.getRoomNumber()
                + " (loc.ID: " + location.getId() + ")";
    }
}
