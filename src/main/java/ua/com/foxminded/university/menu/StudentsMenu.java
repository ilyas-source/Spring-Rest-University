package ua.com.foxminded.university.menu;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.StudentService;

import java.util.Comparator;
import java.util.List;

import static ua.com.foxminded.university.Main.CR;

@Component
public class StudentsMenu {

    private AddressesMenu addressMenu;
    private GroupsMenu groupsMenu;
    private StudentService studentService;

    public StudentsMenu(AddressesMenu addressMenu, GroupsMenu groupsMenu,  StudentService studentService) {
        this.addressMenu = addressMenu;
        this.groupsMenu = groupsMenu;
        this.studentService = studentService;
    }

    public String getStringOfStudents(List<Student> students) {
        StringBuilder result = new StringBuilder();
        students.sort(Comparator.comparing(Student::getId));
        for (Student student : students) {
            result.append(student.getId()).append(". " + getStringFromStudent(student) + CR);
        }
        return result.toString();
    }

    public String getStringFromStudent(Student student) {
        StringBuilder result = new StringBuilder();
        result.append(student.getFirstName() + " " + student.getLastName() + ", " + student.getGender()
                + ", born " + student.getBirthDate() + ", " + CR);
        result.append("Mail: " + student.getEmail() + ", phone number " + student.getPhoneNumber() + CR);
        result.append("Postal address: " + addressMenu.getStringFromAddress(student.getAddress()) + CR);
        result.append("Assigned to group " + student.getGroup().getName());

        return result.toString();
    }
}
