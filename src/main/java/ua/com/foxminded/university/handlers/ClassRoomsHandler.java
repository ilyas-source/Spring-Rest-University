package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.*;

import java.util.List;

import ua.com.foxminded.university.model.ClassRoom;
import ua.com.foxminded.university.model.University;

public class ClassRoomsHandler {

    public static String getStringOfClassRooms(List<ClassRoom> classRooms) {
	StringBuilder result = new StringBuilder();
	for (ClassRoom classRoom : classRooms) {
	    result.append(classRooms.indexOf(classRoom) + ". " + classRoom + CR);
	}
	return result.toString();
    }

    public ClassRoom getClassRoomFromScanner(University university) {
	// TODO Auto-generated method stub
	return null;
    }

    public void updateAClassRoom(University university) {
	// TODO Auto-generated method stub

    }

    public void deleteAClassRoom(University university) {
	// TODO Auto-generated method stub

    }
}
