package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.*;
import ua.com.foxminded.university.model.Location;

public class LocationsHandler {

    public static Location getLocationFromScanner() {
	System.out.print("Enter building name: ");
	String building = scanner.nextLine();
	System.out.print("Enter floor number: ");
	int floor = readNextInt();
	System.out.print("Enter room number: ");
	int roomNumber = readNextInt();

	return new Location(building, floor, roomNumber);
    }

}
