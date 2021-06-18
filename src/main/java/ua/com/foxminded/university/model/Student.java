package ua.com.foxminded.university.model;

import java.time.LocalDate;

public class Student {

    int id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
    private Address address;
    private Group group;

    public Student() {
    }

    public Student(String firstName, String lastName, Gender gender, LocalDate birthDate, LocalDate entryYear, String email,
	    String phoneNumber, Address address, Group group) {
	this.firstName = firstName;
	this.lastName = lastName;
	this.gender = gender;
	this.birthDate = birthDate;
	this.email = email;
	this.phoneNumber = phoneNumber;
	this.address = address;
	this.group = group;
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

    public LocalDate getBirthDate() {
	return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
	this.birthDate = birthDate;
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

    public Group getGroup() {
	return group;
    }

    public void setGroup(Group group) {
	this.group = group;
    }
}
