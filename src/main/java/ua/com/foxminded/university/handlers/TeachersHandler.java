package ua.com.foxminded.university.handlers;

import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.University;
import ua.com.foxminded.university.model.Vacation;

public class TeachersHandler {

    public void addTeacher(University university) {
	Scanner scanner = new Scanner(System.in);
	List<Teacher> teachers = university.getTeachers();
	List<Subject> subjects;
	List<Vacation> vacations;

	System.out.print("First name: ");
	String firstName = scanner.nextLine();

	System.out.print("last name: ");
	String lastName = scanner.nextLine();

	System.out.println("Gender (m/f): ");
	Gender gender = getGenderFromScanner();

	System.out.println("Degree ((B)achelor/(M)aster/(D)octor: ");
	Degree degree = getDegreeFromScanner();

	System.out.println("Enter subjects: ");
	subjects = getSubjectFromScanner();

	System.out.println("Enter email:");
	String email = scanner.nextLine();

	System.out.println("Enter phone:");
	String phone = scanner.nextLine();

	System.out.println("Enter address: ");
	Address address = getAddressFromScanner();

	System.out.println("Enter vacations: ");
	vacations = getVacationsFromScanner();

	Teacher teacher = new Teacher(firstName, lastName, gender, degree, subjects, email, phone, address, vacations);
	teachers.add(teacher);
	university.setTeachers(teachers);

	scanner.close();
    }

    private List<Vacation> getVacationsFromScanner() {
	// TODO Auto-generated method stub
	return null;
    }

    private Address getAddressFromScanner() {
	// TODO Auto-generated method stub
	return null;
    }

    private List<Subject> getSubjectFromScanner() {
	// TODO Auto-generated method stub
	return null;
    }

    private Degree getDegreeFromScanner() {
	// TODO Auto-generated method stub
	return null;
    }

    private Gender getGenderFromScanner() {
	// TODO Auto-generated method stub
	return null;
    }

}
