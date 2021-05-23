package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.*;

import java.util.List;

import ua.com.foxminded.university.model.ClassRoom;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.model.University;

public class ClassRoomsHandler {

    public static String getStringOfClassRooms(List<ClassRoom> classRooms) {
	StringBuilder result = new StringBuilder();
	for (ClassRoom classRoom : classRooms) {
	    result.append(classRooms.indexOf(classRoom) + 1).append(". " + classRoom + CR);
	}
	return result.toString();
    }

    public static ClassRoom getClassRoomFromScanner(University university) {
	System.out.println("Entering new classroom location.");
	Location location = LocationsHandler.getLocationFromScanner();
	System.out.print("Classroom name: ");
	String name = scanner.nextLine();
	System.out.print("Classroom capacity, students: ");
	int capacity = readNextInt();

	return new ClassRoom(location, name, capacity);
    }

    public static void updateAClassRoom(University university) {
	// TODO Auto-generated method stub

    }

    public static void deleteAClassRoom(University university) {
	// TODO Auto-generated method stub
    }
}
