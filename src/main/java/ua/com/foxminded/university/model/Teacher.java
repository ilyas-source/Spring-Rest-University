package ua.com.foxminded.university.model;

import java.util.List;

public class Teacher {

    private int id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Degree degree;
    private List<Subject> subjects;
    private String email;
    private String phoneNumber;
    private Address address;
    private List<Vacation> vacations;

    public Teacher() {
    }

    public Teacher(String firstName, String lastName, Gender gender, Degree degree, List<Subject> subjects, String email,
	    String phoneNumber, Address address) {
	this.firstName = firstName;
	this.lastName = lastName;
	this.gender = gender;
	this.degree = degree;
	this.subjects = subjects;
	this.email = email;
	this.phoneNumber = phoneNumber;
	this.address = address;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
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

    public void setSubjects(List<Subject> subjects) {
	this.subjects = subjects;
    }
}
