package ua.com.foxminded.university.menu;

import static java.util.Objects.isNull;
import static ua.com.foxminded.university.Menu.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.Menu;
import ua.com.foxminded.university.dao.jdbc.JdbcStudentDao;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

@Component
public class StudentsMenu {

    private AddressMenu addressMenu;
    private GroupsMenu groupsMenu;
    private GenderMenu genderMenu;
    private JdbcStudentDao jdbcStudentDao;

    public StudentsMenu(AddressMenu addressMenu, GroupsMenu groupsMenu, GenderMenu genderMenu, JdbcStudentDao jdbcStudentDao) {
	this.addressMenu = addressMenu;
	this.groupsMenu = groupsMenu;
	this.genderMenu = genderMenu;
	this.jdbcStudentDao = jdbcStudentDao;
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

    public void addStudent() {
	jdbcStudentDao.create(createStudent());
    }

    public Student createStudent() {
	System.out.print("First name: ");
	String firstName = scanner.nextLine();
	System.out.print("Last name: ");
	String lastName = scanner.nextLine();
	Gender gender = genderMenu.getGender();
	System.out.print("Birth date: ");
	LocalDate birthDate = Menu.getDateFromScanner();
	System.out.print("Email: ");
	String email = scanner.nextLine();
	System.out.print("Phone number: ");
	String phone = scanner.nextLine();
	Address address = addressMenu.createAddress();
	Group group = groupsMenu.selectGroup();

	return new Student(firstName, lastName, gender, birthDate, email, phone, address, group);
    }

    public void printStudents() {
	System.out.println(getStringOfStudents(jdbcStudentDao.findAll()));
    }

    public Student selectStudent() {
	List<Student> students = jdbcStudentDao.findAll();
	Student result = null;

	while (isNull(result)) {
	    System.out.println("Select student: ");
	    System.out.print(getStringOfStudents(students));
	    int choice = getIntFromScanner();
	    Student selected = jdbcStudentDao.findById(choice).orElse(null);
	    if (isNull(selected)) {
		System.out.println("No such student.");
	    } else {
		result = selected;
		System.out.println("Success.");
	    }
	}
	return result;
    }

    public void updateStudent() {
	Student oldStudent = selectStudent();
	Student newStudent = createStudent();
	newStudent.setId(oldStudent.getId());
	jdbcStudentDao.update(newStudent);
	System.out.println("Overwrite successful.");
    }

    public void deleteStudent() {
	jdbcStudentDao.delete(selectStudent().getId());
	System.out.println("Student deleted successfully.");
    }
}
