package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import java.util.List;

import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.model.University;

public class ClassroomsMenu {

    private LocationsMenu locationsMenu = new LocationsMenu();
    private University university;

    public ClassroomsMenu(University university) {
	this.university = university;
    }

    public String getStringOfClassrooms(List<Classroom> classrooms) {
	StringBuilder result = new StringBuilder();
	for (Classroom classroom : classrooms) {
	    result.append(classrooms.indexOf(classroom) + 1).append(". " + getStringFromClassroom(classroom) + CR);
	}
	return result.toString();
    }

    public String getStringFromClassroom(Classroom classroom) {
	return classroom.getName() + ": " + locationsMenu.getStringFromLocation(classroom.getLocation()) + ". Capacity: "
		+ classroom.getCapacity();
    }

    public Classroom createClassroom() {
	System.out.println("Entering new classroom location.");
	Location location = locationsMenu.createLocation();
	System.out.print("Classroom name: ");
	String name = scanner.nextLine();
	System.out.print("Classroom capacity, students: ");
	int capacity = getIntFromScanner();

	return new Classroom(location, name, capacity);
    }

    public Classroom selectClassroom() {
	List<Classroom> classrooms = university.getClassrooms();
	Boolean correctEntry = false;
	Classroom result = null;

	while (!correctEntry) {
	    System.out.println("Select a classroom: ");
	    System.out.print(getStringOfClassrooms(classrooms));
	    int choice = getIntFromScanner() - 1;
	    if (choice <= classrooms.size()) {
		result = classrooms.get(choice);
		System.out.println("Success.");
		correctEntry = true;
	    } else {
		System.out.println("No such classroom.");
	    }
	}
	return result;
    }

    public void updateClassroom() {
	List<Classroom> classrooms = university.getClassrooms();

	System.out.println("Select a classroom to update: ");
	System.out.println(getStringOfClassrooms(classrooms));
	int choice = getIntFromScanner() - 1;
	if (choice > classrooms.size()) {
	    System.out.println("No such classroom, returning...");
	} else {
	    classrooms.set(choice, createClassroom());
	    System.out.println("Overwrite successful.");
	}
    }

    public void deleteClassroom() {
	List<Classroom> classrooms = university.getClassrooms();

	System.out.println("Select a classroom to delete: ");
	System.out.println(getStringOfClassrooms(classrooms));
	int choice = getIntFromScanner() - 1;
	if (choice > classrooms.size()) {
	    System.out.println("No such classroom, returning...");
	} else {
	    classrooms.remove(choice);
	    System.out.println("Classroom deleted successfully.");
	}
    }
}
