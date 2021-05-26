package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import java.util.List;
import static java.util.Objects.isNull;

import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.University;
import ua.com.foxminded.university.model.Vacation;

public class TeachersMenu {

    private University university;
    private GenderMenu genderMenu = new GenderMenu();
    private VacationsMenu vacationsMenu = new VacationsMenu();
    private AddressMenu addressMenu = new AddressMenu();
    private SubjectsMenu subjectsMenu;

    public TeachersMenu(University university) {
	this.university = university;
	this.subjectsMenu = new SubjectsMenu(university);
    }

    public Teacher createTeacher() {
	System.out.print("Enter first name: ");
	String firstName = scanner.nextLine();

	System.out.print("Enter last name: ");
	String lastName = scanner.nextLine();

	Gender gender = genderMenu.getGender();
	Degree degree = getDegree();

	System.out.println("Email:");
	String email = scanner.nextLine();

	System.out.println("Phone:");
	String phone = scanner.nextLine();

	Address address = addressMenu.createAddress();
	List<Subject> subjects = subjectsMenu.selectSubjects();
	List<Vacation> vacations = vacationsMenu.createVacations();

	return new Teacher(firstName, lastName, gender, degree, subjects, email, phone, address, vacations);
    }

    private Degree getDegree() {
	Degree degree = null;

	while (isNull(degree)) {
	    System.out.print("Degree: (B)achelor/(M)aster/(D)octor: ");
	    String choice = scanner.nextLine().toLowerCase();
	    switch (choice) {
	    case "b":
		degree = Degree.BACHELOR;
		break;
	    case "m":
		degree = Degree.MASTER;
		break;
	    case "d":
		degree = Degree.DOCTOR;
		break;
	    default:
		System.out.println("Wrong input, try again.");
	    }
	}
	return degree;
    }

    public String getStringOfTeachers(List<Teacher> teachers) {
	StringBuilder result = new StringBuilder();
	for (Teacher teacher : teachers) {
	    result.append(teachers.indexOf(teacher) + 1).append(". " + getStringFromTeacher(teacher) + CR);
	}
	return result.toString();
    }

    public String getStringFromTeacher(Teacher teacher) {
	return teacher.getFirstName() + " " + teacher.getLastName() + ", " + teacher.getGender()
		+ ", degree: " + teacher.getDegree() + ", " + teacher.getEmail() + ", " + teacher.getPhoneNumber() + CR
		+ "Postal address: " + addressMenu.getStringFromAddress(teacher.getAddress()) + CR
		+ "Subjects:" + CR + subjectsMenu.getStringOfSubjects(teacher.getSubjects()) + CR
		+ "Vacations:" + CR + vacationsMenu.getStringOfVacations(teacher.getVacations());
    }

    public Teacher selectTeacher() {
	List<Teacher> teachers = university.getTeachers();
	Teacher result = null;

	while (isNull(result)) {
	    System.out.println("Select a teacher: ");
	    System.out.print(getStringOfTeachers(teachers));
	    int choice = getIntFromScanner();
	    if (choice <= teachers.size()) {
		result = teachers.get(choice - 1);
		System.out.println("Success.");
	    } else {
		System.out.println("No such object.");
	    }
	}
	return result;
    }

    public void updateTeacher() {
	List<Teacher> teachers = university.getTeachers();

	System.out.println("Select a teacher to update: ");
	System.out.println(getStringOfTeachers(teachers));
	int choice = getIntFromScanner();
	if (choice > teachers.size()) {
	    System.out.println("No such teacher, returning...");
	} else {
	    teachers.set(choice - 1, createTeacher());
	    System.out.println("Overwrite successful.");
	}
    }

    public void deleteTeacher() {
	List<Teacher> teachers = university.getTeachers();

	System.out.println("Select a teacher to delete: ");
	System.out.println(getStringOfTeachers(teachers));
	int choice = getIntFromScanner();
	if (choice > teachers.size()) {
	    System.out.println("No such teacher, returning...");
	} else {
	    teachers.remove(choice - 1);
	    System.out.println("Teacher deleted successfully.");
	}
    }
}
