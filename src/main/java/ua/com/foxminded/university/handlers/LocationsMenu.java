package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.*;
import ua.com.foxminded.university.model.Location;

public class LocationsMenu {

    public static Location createLocation() {
	System.out.print("Enter building name: ");
	String building = scanner.nextLine();
	System.out.print("Enter floor number: ");
	int floor = getIntFromScanner();
	System.out.print("Enter room number: ");
	int roomNumber = getIntFromScanner();

	return new Location(building, floor, roomNumber);
    }

}
