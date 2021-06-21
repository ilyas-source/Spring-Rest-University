package ua.com.foxminded.university.menu;

import static java.util.Objects.isNull;
import static ua.com.foxminded.university.Menu.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.Menu;
import ua.com.foxminded.university.dao.jdbc.JdbcStudentDAO;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

@Component
public class StudentsMenu {

    @Autowired
    private AddressMenu addressMenu;
    @Autowired
    private GroupsMenu groupsMenu;
    @Autowired
    private GenderMenu genderMenu;
    @Autowired
    private JdbcStudentDAO jdbcStudentDAO;

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
	jdbcStudentDAO.addToDb(createStudent());
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
	System.out.println(getStringOfStudents(jdbcStudentDAO.findAll()));
    }

    public void updateStudent() {
	List<Student> students = jdbcStudentDAO.findAll();

	System.out.println("Select a student to update: ");
	System.out.println(getStringOfStudents(students));
	int choice = getIntFromScanner();
	Student selected = jdbcStudentDAO.findById(choice).orElse(null);

	if (isNull(selected)) {
	    System.out.println("No such student, returning...");
	} else {
	    Student newStudent = createStudent();
	    newStudent.setId(selected.getId());
	    jdbcStudentDAO.update(newStudent);
	    System.out.println("Overwrite successful.");
	}
    }

    public void deleteStudent() {
	List<Student> students = jdbcStudentDAO.findAll();

	System.out.println("Select a student to delete: ");
	System.out.println(getStringOfStudents(students));
	int choice = getIntFromScanner();
	Student student = jdbcStudentDAO.findById(choice).orElse(null);

	if (isNull(student)) {
	    System.out.println("No such student, returning...");
	} else {
	    jdbcStudentDAO.delete(choice);
	    System.out.println("Student deleted successfully.");
	}
    }
}
