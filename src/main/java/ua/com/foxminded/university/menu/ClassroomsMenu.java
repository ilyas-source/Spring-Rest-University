package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.model.Teacher;

@Component
public class ClassroomsMenu {

    @Autowired
    private LocationsMenu locationsMenu;
    @Autowired
    private JdbcClassroomDao jdbcClassroomDAO;

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

    public void addClassroom() {
	jdbcClassroomDAO.addToDb(createClassroom());
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
	List<Classroom> classrooms = jdbcClassroomDAO.findAll();
	Classroom result = null;

	while (isNull(result)) {
	    System.out.println("Select subject: ");
	    System.out.print(getStringOfClassrooms(classrooms));
	    int choice = getIntFromScanner();
	    Classroom selected = jdbcClassroomDAO.findById(choice).orElse(null);
	    if (isNull(selected)) {
		System.out.println("No such subject.");
	    } else {
		result = selected;
		System.out.println("Success.");
	    }
	}
	return result;
    }

    public void updateClassroom() {
	Classroom oldClassroom = selectClassroom();
	Classroom newClassroom = createClassroom();
	newClassroom.setId(oldClassroom.getId());
	jdbcClassroomDAO.update(newClassroom);
	System.out.println("Overwrite successful.");
    }

    public void deleteClassroom() {
	jdbcClassroomDAO.delete(selectClassroom().getId());
	System.out.println("Classroom deleted successfully.");
    }

    public void printClassrooms() {
	System.out.println(getStringOfClassrooms(jdbcClassroomDAO.findAll()));
    }
}
