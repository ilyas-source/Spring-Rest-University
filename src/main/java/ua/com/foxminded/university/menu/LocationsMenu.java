package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.getIntFromScanner;
import static ua.com.foxminded.university.Menu.scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcLocationDAO;
import ua.com.foxminded.university.model.Location;

@Component
public class LocationsMenu {

    @Autowired
    private JdbcLocationDAO jdbcLocationDAO;

    public Location createLocation() {
	System.out.print("Enter building name: ");
	String building = scanner.nextLine();
	System.out.print("Enter floor number: ");
	int floor = getIntFromScanner();
	System.out.print("Enter room number: ");
	int roomNumber = getIntFromScanner();

	return new Location(building, floor, roomNumber);
    }

    public String getStringFromLocation(Location location) {
	return location.getBuilding() + ", floor #" + location.getFloor() + ", room " + location.getRoomNumber();
    }
}
