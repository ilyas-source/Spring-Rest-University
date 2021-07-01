package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

@Component
public class TeachersMenu {

    private GenderMenu genderMenu;
    private VacationsMenu vacationsMenu;
    private AddressesMenu addressMenu;
    private SubjectsMenu subjectsMenu;
    private JdbcTeacherDao jdbcTeacherDao;

    public TeachersMenu(GenderMenu genderMenu, VacationsMenu vacationsMenu, AddressesMenu addressMenu, SubjectsMenu subjectsMenu,
	    JdbcTeacherDao jdbcTeacherDao) {
	this.genderMenu = genderMenu;
	this.vacationsMenu = vacationsMenu;
	this.addressMenu = addressMenu;
	this.subjectsMenu = subjectsMenu;
	this.jdbcTeacherDao = jdbcTeacherDao;
    }

    public String getStringOfTeachers(List<Teacher> teachers) {
	StringBuilder result = new StringBuilder();
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

    public void addTeacher() {
	jdbcTeacherDao.create(createTeacher());
    }

    public Teacher createTeacher() {
	System.out.print("Enter first name: ");
	String firstName = scanner.nextLine();

	System.out.print("Enter last name: ");
	String lastName = scanner.nextLine();

	Gender gender = genderMenu.getGender();
	Degree degree = getDegree();

	System.out.print("Email:");
	String email = scanner.nextLine();

	System.out.print("Phone:");
	String phone = scanner.nextLine();

	Address address = addressMenu.createAddress();
	List<Subject> subjects = subjectsMenu.selectSubjects();

	Teacher result = new Teacher.Builder(firstName, lastName)
		.gender(gender)
		.degree(degree)
		.subjects(subjects)
		.email(email)
		.phoneNumber(phone)
		.address(address)
		.build();

	List<Vacation> vacations = vacationsMenu.createVacations();
	result.setVacations(vacations);

	return result;
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

    public void printTeachers() {
	System.out.println(getStringOfTeachers(jdbcTeacherDao.findAll()));
    }

    public Teacher selectTeacher() {
	List<Teacher> teachers = jdbcTeacherDao.findAll();
	Teacher result = null;

	while (isNull(result)) {
	    System.out.println("Select teacher: ");
	    System.out.print(getStringOfTeachers(teachers));
	    int choice = getIntFromScanner();
	    Optional<Teacher> selectedTeacher = jdbcTeacherDao.findById(choice);
	    if (selectedTeacher.isEmpty()) {
		System.out.println("No such teacher.");
	    } else {
		result = selectedTeacher.get();
		System.out.println("Success.");
	    }
	}
	return result;
    }

    public void updateTeacher() {
	Teacher oldTeacher = selectTeacher();
	Teacher newTeacher = createTeacher();
	newTeacher.setId(oldTeacher.getId());
	newTeacher.getAddress().setId(oldTeacher.getAddress().getId());
	jdbcTeacherDao.update(newTeacher);
	System.out.println("Overwrite successful.");
    }

    public void deleteTeacher() {
	jdbcTeacherDao.delete(selectTeacher().getId());
	System.out.println("Teacher deleted successfully.");
    }
}
