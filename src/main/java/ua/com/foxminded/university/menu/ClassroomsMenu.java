package ua.com.foxminded.university.menu;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.service.ClassroomService;

import java.util.Comparator;
import java.util.List;

import static ua.com.foxminded.university.Menu.CR;

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

    public void printClassrooms() {
        System.out.println(getStringOfClassrooms(classroomService.findAll()));
    }
}
