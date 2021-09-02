package ua.com.foxminded.university.menu;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.service.ClassroomService;

@Component
public class ClassroomsMenu {

    private LocationsMenu locationsMenu;
    private ClassroomService classroomService;

    public ClassroomsMenu(LocationsMenu locationsMenu, ClassroomService classroomService) {
        this.locationsMenu = locationsMenu;
        this.classroomService = classroomService;
    }

    public String getStringFromClassroom(Classroom classroom) {
        return classroom.getId() + ". " + classroom.getName() + ": "
                + locationsMenu.getStringFromLocation(classroom.getLocation())
                + ". Capacity: " + classroom.getCapacity();
    }
}
