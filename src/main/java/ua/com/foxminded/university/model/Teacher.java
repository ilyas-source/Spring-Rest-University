package ua.com.foxminded.university.model;

import java.util.List;

import ua.com.foxminded.university.handlers.SubjectsMenu;
import ua.com.foxminded.university.handlers.VacationsMenu;

import static ua.com.foxminded.university.Menu.CR;

public class Teacher {

    private String firstName;
    private String lastName;
    private Gender gender;
    private Degree degree;
    private List<Subject> subjects;
    private String email;
    private String phoneNumber;
    private Address address;
    private List<Vacation> vacations;

    public Teacher(String firstName, String lastName, Gender gender, Degree degree, List<Subject> subjects, String email,
	    String phoneNumber, Address address, List<Vacation> vacations) {
	this.firstName = firstName;
	this.lastName = lastName;
	this.gender = gender;
	this.degree = degree;
	this.subjects = subjects;
	this.email = email;
	this.phoneNumber = phoneNumber;
	this.address = address;
	this.vacations = vacations;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public Gender getGender() {
	return gender;
    }

    public void setGender(Gender gender) {
	this.gender = gender;
    }

    public Degree getDegree() {
	return degree;
    }

    public void setDegree(Degree degree) {
	this.degree = degree;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPhoneNumber() {
	return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
	return address;
    }

    public void setAddress(Address address) {
	this.address = address;
    }

    public List<Vacation> getVacations() {
	return vacations;
    }

    public void setVacations(List<Vacation> vacations) {
	this.vacations = vacations;
    }

    public List<Subject> getSubjects() {
	return subjects;
    }

    public void setSubjectIndexes(List<Subject> subjects) {
	this.subjects = subjects;
    }

    @Override
    public String toString() {
	return firstName + " " + lastName + ", " + gender + ", degree: " + degree + ", " + email + ", " + phoneNumber + CR
		+ "Postal address: " + address + CR
		+ "Subjects:" + subjects + CR
		+ "Vacations:" + vacations;
    }
}
