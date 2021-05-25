package ua.com.foxminded.university;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import ua.com.foxminded.university.menu.ClassroomsMenu;
import ua.com.foxminded.university.menu.GroupsMenu;
import ua.com.foxminded.university.menu.LecturesMenu;
import ua.com.foxminded.university.menu.StudentsMenu;
import ua.com.foxminded.university.menu.SubjectsMenu;
import ua.com.foxminded.university.menu.TeachersMenu;
import ua.com.foxminded.university.menu.UniversityMenu;
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
	    + "7. Rename university" + CR
	    + "Enter choice or 0 to quit:";

    private static final String CRUD_MENU_TEXT = CR
	    + "1. Create" + CR
	    + "2. Read (all)" + CR
	    + "3. Update" + CR
	    + "4. Delete" + CR
	    + "Enter choice or 0 to return:";

    public static final Scanner scanner = new Scanner(System.in);

    private TeachersMenu teachersMenu;
    private GroupsMenu groupsMenu;
    private StudentsMenu studentsMenu;
    private SubjectsMenu subjectsMenu;
    private LecturesMenu lecturesMenu;
    private UniversityMenu universityMenu;
    private ClassroomsMenu classroomsMenu;

    private University university;

    public Menu(University university) {
	this.university = university;
	this.universityMenu = new UniversityMenu();
	this.teachersMenu = new TeachersMenu();
	this.groupsMenu = new GroupsMenu();
	this.subjectsMenu = new SubjectsMenu();
	this.lecturesMenu = new LecturesMenu();
	this.classroomsMenu = new ClassroomsMenu();
	this.studentsMenu = new StudentsMenu();
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
	    System.exit(0);
	case 1:
	    universityMenu.printUniversity(university);
	    start(0);
	    break;
	case 2:
	    System.out.println(FORMAT_DIVIDER + "Manage teachers:" + CRUD_MENU_TEXT);
	    menuChoice = getIntFromScanner();
	    manageTeachers(menuChoice);
	    break;
	case 3:
	    System.out.println(FORMAT_DIVIDER + "Manage groups:" + CRUD_MENU_TEXT);
	    menuChoice = getIntFromScanner();
	    manageGroups(menuChoice);
	    break;
	case 4:
	    System.out.println(FORMAT_DIVIDER + "Manage subjects:" + CRUD_MENU_TEXT);
	    menuChoice = getIntFromScanner();
	    manageSubjects(menuChoice);
	    break;
	case 5:
	    System.out.println(FORMAT_DIVIDER + "Manage lectures:" + CRUD_MENU_TEXT);
	    menuChoice = getIntFromScanner();
	    manageLectures(menuChoice);
	    break;
	case 6:
	    System.out.println(FORMAT_DIVIDER + "Manage classrooms:" + CRUD_MENU_TEXT);
	    menuChoice = getIntFromScanner();
	    manageClassrooms(menuChoice);
	    break;
	case 7:
	    System.out.println(FORMAT_DIVIDER + "Manage students:" + CRUD_MENU_TEXT);
	    menuChoice = getIntFromScanner();
	    manageStudents(menuChoice);
	    break;
	case 8:
	    System.out.println("Enter new name for the university: ");
	    university.setName(scanner.nextLine());
	default:
	    start(0);
	    break;
	}
    }

    private void manageTeachers(int menuChoice) {
	switch (menuChoice) {
	case 1:
	    university.getTeachers().add(teachersMenu.createTeacher(university));
	    start(2);
	    break;
	case 2:
	    System.out.println(teachersMenu.getStringOfTeachers(university.getTeachers()));
	    start(2);
	    break;
	case 3:
	    teachersMenu.updateTeacher(university);
	    start(2);
	    break;
	case 4:
	    teachersMenu.deleteTeacher(university);
	    start(2);
	    break;
	default:
	    System.out.println("Returning...");
	    start(0);
	    break;
	}
    }

    private void manageGroups(int menuChoice) {
	switch (menuChoice) {
	case 1:
	    university.getGroups().add(groupsMenu.createGroup(university));
	    start(3);
	    break;
	case 2:
	    System.out.println(groupsMenu.getStringOfGroups(university.getGroups()));
	    start(3);
	    break;
	case 3:
	    groupsMenu.updateGroup(university);
	    start(3);
	    break;
	case 4:
	    groupsMenu.deleteGroup(university);
	    start(3);
	    break;
	default:
	    System.out.println("Returning...");
	    start(0);
	    break;
	}
    }

    private void manageSubjects(int menuChoice) {
	switch (menuChoice) {
	case 1:
	    university.getSubjects().add(subjectsMenu.createSubject(university));
	    start(4);
	    break;
	case 2:
	    System.out.println(subjectsMenu.getStringOfSubjects(university.getSubjects()));
	    start(4);
	    break;
	case 3:
	    subjectsMenu.updateSubject(university);
	    start(4);
	    break;
	case 4:
	    subjectsMenu.deleteSubject(university);
	    start(4);
	    break;
	default:
	    System.out.println("Returning...");
	    start(0);
	    break;
	}
    }

    private void manageLectures(int menuChoice) {
	switch (menuChoice) {
	case 1:
	    university.getLectures().add(lecturesMenu.createLecture(university));
	    start(5);
	    break;
	case 2:
	    System.out.println(lecturesMenu.getStringOfLectures(university.getLectures()));
	    start(5);
	    break;
	case 3:
	    lecturesMenu.updateLecture(university);
	    start(5);
	    break;
	case 4:
	    lecturesMenu.deleteLecture(university);
	    start(5);
	    break;
	default:
	    System.out.println("Returning...");
	    start(0);
	    break;
	}
    }

    private void manageClassrooms(int menuChoice) {
	switch (menuChoice) {
	case 1:
	    university.getClassrooms().add(classroomsMenu.createClassroom(university));
	    start(6);
	    break;
	case 2:
	    System.out.println(classroomsMenu.getStringOfClassrooms(university.getClassrooms()));
	    start(6);
	    break;
	case 3:
	    classroomsMenu.updateClassroom(university);
	    start(6);
	    break;
	case 4:
	    classroomsMenu.deleteClassroom(university);
	    start(6);
	    break;
	default:
	    System.out.println("Returning...");
	    start(0);
	    break;
	}
    }

    private void manageStudents(int menuChoice) {
	switch (menuChoice) {
	case 1:
	    university.getStudents().add(studentsMenu.createStudent(university));
	    start(7);
	    break;
	case 2:
	    System.out.println(studentsMenu.getStringOfStudents(university.getStudents()));
	    start(7);
	    break;
	case 3:
	    studentsMenu.updateStudent(university);
	    start(7);
	    break;
	case 4:
	    studentsMenu.deleteStudent(university);
	    start(7);
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

	Boolean correctEntry = false;
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

	Boolean correctEntry = false;
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
