package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.*;

import java.util.List;

import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.model.University;

public class ClassroomsHandler {

    public static String getStringOfClassrooms(List<Classroom> classRooms) {
	StringBuilder result = new StringBuilder();
	for (Classroom classRoom : classRooms) {
	    result.append(classRooms.indexOf(classRoom) + 1).append(". " + classRoom + CR);
	}
	return result.toString();
    }

    public static Classroom getNewClassroomFromScanner(University university) {
	System.out.println("Entering new classroom location.");
	Location location = LocationsHandler.getLocationFromScanner();
	System.out.print("Classroom name: ");
	String name = scanner.nextLine();
	System.out.print("Classroom capacity, students: ");
	int capacity = readNextInt();

	return new Classroom(location, name, capacity);
    }

    public static void updateAClassroom(University university) {

	List<Classroom> classrooms = university.getClassrooms();

	System.out.println("Select a classroom to edit: ");
	System.out.println(getStringOfClassrooms(classrooms));
	int choice = readNextInt() - 1;
	if (choice > classrooms.size()) {
	    System.out.println("No such classroom, returning...");
	} else {
	    classrooms.set(choice, getNewClassroomFromScanner(university));
	    System.out.println("Overwrite successful.");
	}
    }

    public static void deleteAClassroom(University university) {
	List<Classroom> classrooms = university.getClassrooms();

	System.out.println("Select a classroom to delete: ");
	System.out.println(getStringOfClassrooms(classrooms));
	int choice = readNextInt() - 1;
	if (choice > classrooms.size()) {
	    System.out.println("No such classroom, returning...");
	} else {
	    classrooms.remove(choice);
	    System.out.println("Classroom deleted successfully.");
	}
    }
}
