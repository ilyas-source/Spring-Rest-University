package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.*;

import java.util.List;

import ua.com.foxminded.university.model.ClassRoom;

public class ClassRoomsHandler {

    public static String getStringOfClassRooms(List<ClassRoom> classRooms) {
	StringBuilder result = new StringBuilder();
	for (ClassRoom classRoom : classRooms) {
	    result.append(classRooms.indexOf(classRoom) + ". " + classRoom + CR);
	}
	return result.toString();
    }
}
