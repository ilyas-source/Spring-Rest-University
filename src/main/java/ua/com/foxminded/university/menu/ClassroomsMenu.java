package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.service.ClassroomService;

@Component
public class ClassroomsMenu {

    private LocationsMenu locationsMenu;
    private ClassroomService classroomService;

    public ClassroomsMenu(LocationsMenu locationsMenu, ClassroomService classroomService) {
	this.locationsMenu = locationsMenu;
	this.classroomService = classroomService;
    }

    public String getStringOfClassrooms(List<Classroom> classrooms) {
	StringBuilder result = new StringBuilder();
	classrooms.sort(Comparator.comparing(Classroom::getId));

	for (Classroom c : classrooms) {
	    result.append(getStringFromClassroom(c) + CR);
	}
	return result.toString();
    }

    public String getStringFromClassroom(Classroom classroom) {
	return classroom.getId() + ". " + classroom.getName() + ": "
		+ locationsMenu.getStringFromLocation(classroom.getLocation())
		+ ". Capacity: " + classroom.getCapacity();
    }

    public void addClassroom() throws Exception {
	classroomService.create(createClassroom());
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
	List<Classroom> classrooms = classroomService.findAll();
	Classroom result = null;

	while (isNull(result)) {
	    System.out.println("Select classroom: ");
	    System.out.print(getStringOfClassrooms(classrooms));
	    int choice = getIntFromScanner();
	    Optional<Classroom> selectedClassroom = classroomService.findById(choice);
	    if (selectedClassroom.isEmpty()) {
		System.out.println("No such subject.");
	    } else {
		result = selectedClassroom.get();
		System.out.println("Success.");
	    }
	}
	return result;
    }

    public void updateClassroom() throws Exception {
	Classroom oldClassroom = selectClassroom();
	Classroom newClassroom = createClassroom();
	newClassroom.setId(oldClassroom.getId());
	newClassroom.getLocation().setId(oldClassroom.getLocation().getId());
	classroomService.update(newClassroom);
	System.out.println("Overwrite successful.");
    }

    public void deleteClassroom() throws Exception {
	classroomService.delete(selectClassroom().getId());
	System.out.println("Classroom deleted successfully.");
    }

    public void printClassrooms() {
	System.out.println(getStringOfClassrooms(classroomService.findAll()));
    }
}
