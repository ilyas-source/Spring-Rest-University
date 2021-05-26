package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.university.Menu;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.University;

public class StudentsMenu {

    private AddressMenu addressMenu = new AddressMenu();;
    private GenderMenu genderMenu = new GenderMenu();;

    private University university;

    public StudentsMenu(University university) {
	this.university = university;
    }

    public String getStringOfStudents(List<Student> students) {
	StringBuilder result = new StringBuilder();
	for (Student student : students) {
	    result.append(students.indexOf(student) + 1).append(". " + getStringFromStudent(student) + CR);
	}
	return result.toString();
    }

    public String getStringFromStudent(Student student) {

	StringBuilder result = new StringBuilder();
	result.append(student.getFirstName() + " " + student.getLastName() + ", " + student.getGender()
		+ ", born " + student.getBirthDate() + ", admission year " + student.getEntryYear().getYear() + CR);
	result.append("Mail: " + student.getEmail() + ", phone number " + student.getPhoneNumber() + CR);
	result.append("Postal address: " + addressMenu.getStringFromAddress(student.getAddress()));

	return result.toString();
    }

    public Student createStudent() {
	System.out.print("First name: ");
	String firstName = scanner.nextLine();
	System.out.print("Last name: ");
	String lastName = scanner.nextLine();
	System.out.print("Gender (m/f): ");
	Gender gender = genderMenu.getGenderFromScanner();
	System.out.print("Birth date: ");
	LocalDate birthDate = Menu.getDateFromScanner();
	System.out.print("Entry year: ");
	LocalDate entryYear = LocalDate.of(getIntFromScanner(), 1, 1);
	System.out.print("Email: ");
	String email = scanner.nextLine();
	System.out.print("Phone number: ");
	String phone = scanner.nextLine();
	System.out.println("Entering address. ");
	Address address = addressMenu.createAddress();

	return new Student(firstName, lastName, gender, birthDate, entryYear, email, phone, address);
    }

    public List<Student> selectStudents() {
	List<Student> result = new ArrayList<>();
	List<Student> students = university.getStudents();
	Boolean finished = false;
	Boolean correctEntry = false;

	while (!(finished && correctEntry)) {
	    if (result.size() > 0) {
		System.out.println("Assigned students:");
		System.out.print(getStringOfStudents(result));
	    }
	    System.out.print("Enter a new student number to add: " + CR);
	    System.out.print(getStringOfStudents(students) + CR);
	    correctEntry = false;
	    int choice = getIntFromScanner() - 1;
	    if (choice > students.size()) {
		System.out.println("No such student.");
	    } else {
		Student selected = students.get(choice);
		if (result.contains(selected)) {
		    System.out.println("Student already assigned to the group.");
		} else {
		    correctEntry = true;
		    result.add(new Student(selected.getFirstName(), selected.getLastName(),
			    selected.getGender(), selected.getBirthDate(),
			    selected.getEntryYear(), selected.getEmail(),
			    selected.getPhoneNumber(), selected.getAddress()));
		    System.out.println("Success.");
		}
	    }
	    System.out.print("Add another? (y/n): ");
	    String entry = scanner.nextLine().toLowerCase();
	    if (!entry.equals("y")) {
		finished = true;
	    }
	}
	return result;
    }

    public void updateStudent() {
	List<Student> students = university.getStudents();

	System.out.println("Select a student to update: ");
	System.out.println(getStringOfStudents(students));
	int choice = getIntFromScanner() - 1;
	if (choice > students.size()) {
	    System.out.println("No such student, returning...");
	} else {
	    students.set(choice, createStudent());
	    System.out.println("Overwrite successful.");
	}
    }

    public void deleteStudent() {
	List<Student> students = university.getStudents();

	System.out.println("Select a student to update: ");
	System.out.println(getStringOfStudents(students));
	int choice = getIntFromScanner() - 1;
	if (choice > students.size()) {
	    System.out.println("No such student, returning...");
	} else {
	    students.remove(choice);
	    System.out.println("Student deleted successfully.");
	}
    }
}
