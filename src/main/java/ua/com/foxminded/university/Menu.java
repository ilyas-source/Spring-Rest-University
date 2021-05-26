package ua.com.foxminded.university;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import ua.com.foxminded.university.menu.ClassroomsMenu;
import ua.com.foxminded.university.menu.GroupsMenu;
import ua.com.foxminded.university.menu.HolidaysMenu;
import ua.com.foxminded.university.menu.LecturesMenu;
import ua.com.foxminded.university.menu.StudentsMenu;
import ua.com.foxminded.university.menu.SubjectsMenu;
import ua.com.foxminded.university.menu.TeachersMenu;
import ua.com.foxminded.university.menu.UniversityMenu;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.model.University;

public class Menu {

    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final String CR = System.lineSeparator();
    public static final String FORMAT_DIVIDER = "----------------" + CR;
    private static final String MAIN_MENU_TEXT = "Main menu" + CR
	    + "1. View full university contents" + CR
	    + "2. Manage teachers" + CR
	    + "3. Manage groups" + CR
	    + "4. Manage subjects" + CR
	    + "5. Manage lectures" + CR
	    + "6. Manage classrooms" + CR
	    + "7. Manage students" + CR
	    + "8. Rename university" + CR
	    + "9. Manage university holidays" + CR
	    + "Enter choice or 0 to quit:";

    private static final String CRUD_MENU_TEXT = CR
	    + "1. Create" + CR
	    + "2. Read (all)" + CR
	    + "3. Update" + CR
	    + "4. Delete" + CR
	    + "Enter choice or 0 to return:";

    public static final Scanner scanner = new Scanner(System.in);

    private University university;
    private TeachersMenu teachersMenu;
    private GroupsMenu groupsMenu;
    private StudentsMenu studentsMenu;
    private SubjectsMenu subjectsMenu;
    private LecturesMenu lecturesMenu;
    private UniversityMenu universityMenu;
    private ClassroomsMenu classroomsMenu;
    private HolidaysMenu holidaysMenu;

    public Menu(University university) {
	this.university = university;
	this.universityMenu = new UniversityMenu(university);
	this.teachersMenu = new TeachersMenu(university);
	this.groupsMenu = new GroupsMenu(university);
	this.subjectsMenu = new SubjectsMenu(university);
	this.lecturesMenu = new LecturesMenu(university);
	this.classroomsMenu = new ClassroomsMenu(university);
	this.studentsMenu = new StudentsMenu(university);
	this.holidaysMenu = new HolidaysMenu(university);
    }

    public void start(int menuEntryPoint) {

	int menuChoice;

	if (menuEntryPoint == 0) {
	    System.out.println(MAIN_MENU_TEXT);
	    menuChoice = getIntFromScanner();
	} else {
	    menuChoice = menuEntryPoint;
	}

	switch (menuChoice) {
	case 0:
	    System.out.println("Quitting.");
	    scanner.close();
	    break;
	case 1:
	    universityMenu.printUniversity();
	    start(0);
	    break;
	case 2:
	    manageTeachers();
	    break;
	case 3:
	    manageGroups();
	    break;
	case 4:
	    manageSubjects();
	    break;
	case 5:
	    manageLectures();
	    break;
	case 6:
	    manageClassrooms();
	    break;
	case 7:
	    manageStudents();
	    break;
	case 8:
	    System.out.println("Enter new name for the university: ");
	    university.setName(scanner.nextLine());
	case 9:
	    manageHolidays();
	    break;
	default:
	    start(0);
	    break;
	}
    }

    private void manageTeachers() {
	System.out.println(FORMAT_DIVIDER + "Manage teachers:" + CRUD_MENU_TEXT);
	switch (getIntFromScanner()) {
	case 1:
	    university.getTeachers().add(teachersMenu.createTeacher());
	    start(2);
	    break;
	case 2:
	    System.out.println(teachersMenu.getStringOfTeachers(university.getTeachers()));
	    start(2);
	    break;
	case 3:
	    teachersMenu.updateTeacher();
	    start(2);
	    break;
	case 4:
	    teachersMenu.deleteTeacher();
	    start(2);
	    break;
	default:
	    System.out.println("Returning...");
	    start(0);
	    break;
	}
    }

    private void manageGroups() {
	System.out.println(FORMAT_DIVIDER + "Manage groups:" + CRUD_MENU_TEXT);
	switch (getIntFromScanner()) {
	case 1:
	    university.getGroups().add(groupsMenu.createGroup());
	    start(3);
	    break;
	case 2:
	    System.out.println(groupsMenu.getStringOfGroups(university.getGroups()));
	    start(3);
	    break;
	case 3:
	    groupsMenu.updateGroup();
	    start(3);
	    break;
	case 4:
	    groupsMenu.deleteGroup();
	    start(3);
	    break;
	default:
	    System.out.println("Returning...");
	    start(0);
	    break;
	}
    }

    private void manageSubjects() {
	System.out.println(FORMAT_DIVIDER + "Manage subjects:" + CRUD_MENU_TEXT);
	switch (getIntFromScanner()) {
	case 1:
	    university.getSubjects().add(subjectsMenu.createSubject());
	    start(4);
	    break;
	case 2:
	    System.out.println(subjectsMenu.getStringOfSubjects(university.getSubjects()));
	    start(4);
	    break;
	case 3:
	    subjectsMenu.updateSubject();
	    start(4);
	    break;
	case 4:
	    subjectsMenu.deleteSubject();
	    start(4);
	    break;
	default:
	    System.out.println("Returning...");
	    start(0);
	    break;
	}
    }

    private void manageLectures() {
	System.out.println(FORMAT_DIVIDER + "Manage lectures:" + CRUD_MENU_TEXT);
	switch (getIntFromScanner()) {
	case 1:
	    university.getLectures().add(lecturesMenu.createLecture());
	    start(5);
	    break;
	case 2:
	    System.out.println(lecturesMenu.getStringOfLectures(university.getLectures()));
	    start(5);
	    break;
	case 3:
	    lecturesMenu.updateLecture();
	    start(5);
	    break;
	case 4:
	    lecturesMenu.deleteLecture();
	    start(5);
	    break;
	default:
	    System.out.println("Returning...");
	    start(0);
	    break;
	}
    }

    private void manageClassrooms() {
	System.out.println(FORMAT_DIVIDER + "Manage classrooms:" + CRUD_MENU_TEXT);
	switch (getIntFromScanner()) {
	case 1:
	    university.getClassrooms().add(classroomsMenu.createClassroom());
	    start(6);
	    break;
	case 2:
	    System.out.println(classroomsMenu.getStringOfClassrooms(university.getClassrooms()));
	    start(6);
	    break;
	case 3:
	    classroomsMenu.updateClassroom();
	    start(6);
	    break;
	case 4:
	    classroomsMenu.deleteClassroom();
	    start(6);
	    break;
	default:
	    System.out.println("Returning...");
	    start(0);
	    break;
	}
    }

    private void manageStudents() {
	System.out.println(FORMAT_DIVIDER + "Manage students:" + CRUD_MENU_TEXT);
	switch (getIntFromScanner()) {
	case 1:
	    university.getStudents().add(studentsMenu.createStudent());
	    start(7);
	    break;
	case 2:
	    System.out.println(studentsMenu.getStringOfStudents(university.getStudents()));
	    start(7);
	    break;
	case 3:
	    studentsMenu.updateStudent();
	    start(7);
	    break;
	case 4:
	    studentsMenu.deleteStudent();
	    start(7);
	    break;
	default:
	    System.out.println("Returning...");
	    start(0);
	    break;
	}
    }

    private void manageHolidays() {
	System.out.println(FORMAT_DIVIDER + "Manage holidays:" + CRUD_MENU_TEXT);
	switch (getIntFromScanner()) {
	case 1:
	    university.getHolidays().add(holidaysMenu.createHoliday());
	    start(9);
	    break;
	case 2:
	    System.out.println(holidaysMenu.getStringOfHolidays(university.getHolidays()));
	    start(9);
	    break;
	case 3:
	    holidaysMenu.updateHoliday();
	    start(9);
	    break;
	case 4:
	    holidaysMenu.deleteHoliday();
	    start(9);
	    break;
	default:
	    System.out.println("Returning...");
	    start(0);
	    break;
	}
    }

    public static int getIntFromScanner() {
	while (!scanner.hasNextInt()) {
	    scanner.next();
	}
	return Integer.parseInt(scanner.nextLine());
    }

    public static LocalDate getDateFromScanner() {
	LocalDate result = null;
	boolean correctEntry = false;

	while (!correctEntry) {
	    try {
		String line = scanner.nextLine();
		correctEntry = true;
		result = LocalDate.parse(line, dateFormatter);
	    } catch (Exception e) {
		e.printStackTrace();
		correctEntry = false;
	    }
	}
	return result;
    }

    public static LocalTime getTimeFromScanner() {
	LocalTime result = null;

	boolean correctEntry = false;
	while (!correctEntry) {
	    try {
		String line = scanner.nextLine();
		correctEntry = true;
		result = LocalTime.parse(line, timeFormatter);
	    } catch (Exception e) {
		e.printStackTrace();
		correctEntry = false;
	    }
	}
	return result;
    }
}
