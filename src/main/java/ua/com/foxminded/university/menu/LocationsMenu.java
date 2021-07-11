package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.CR;
import static ua.com.foxminded.university.Menu.getIntFromScanner;
import static ua.com.foxminded.university.Menu.scanner;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.model.Location;

@Component
public class LocationsMenu {

    public Location createLocation() {
	System.out.print("Enter building name: ");
	String building = scanner.nextLine();
	System.out.print("Enter floor number: ");
	int floor = getIntFromScanner();
	System.out.print("Enter room number: ");
	int roomNumber = getIntFromScanner();

	return new Location(building, floor, roomNumber);
    }

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
