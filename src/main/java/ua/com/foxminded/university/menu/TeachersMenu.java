package ua.com.foxminded.university.menu;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

import java.util.Comparator;
import java.util.List;

import static ua.com.foxminded.university.Menu.CR;

@Component
public class TeachersMenu {

    private VacationsMenu vacationsMenu;
    private AddressesMenu addressMenu;
    private SubjectsMenu subjectsMenu;
    private TeacherService teacherService;

    public TeachersMenu( VacationsMenu vacationsMenu, AddressesMenu addressMenu, SubjectsMenu subjectsMenu,
                        TeacherService teacherService) {
        this.vacationsMenu = vacationsMenu;
        this.addressMenu = addressMenu;
        this.subjectsMenu = subjectsMenu;
        this.teacherService = teacherService;
    }

    public String getStringOfTeachers(List<Teacher> teachers) {
        var result = new StringBuilder();
        teachers.sort(Comparator.comparing(Teacher::getId));
        for (Teacher teacher : teachers) {
            result.append(teacher.getId()).append(". " + getStringFromTeacher(teacher) + CR);
        }
        return result.toString();
    }

    public String getStringFromTeacher(Teacher teacher) {
        return teacher.getFirstName() + " " + teacher.getLastName() + ", " + teacher.getGender()
                + ", degree: " + teacher.getDegree() + ", " + teacher.getEmail() + ", " + teacher.getPhoneNumber() + CR
                + "Postal address: " + addressMenu.getStringFromAddress(teacher.getAddress()) + CR
                + "Subjects:" + CR + subjectsMenu.getStringOfSubjects(teacher.getSubjects())
                + "Vacations:" + CR + vacationsMenu.getStringOfVacations(teacher.getVacations());
    }


    public void printTeachers() {
        System.out.println(getStringOfTeachers(teacherService.findAll()));
    }
}
