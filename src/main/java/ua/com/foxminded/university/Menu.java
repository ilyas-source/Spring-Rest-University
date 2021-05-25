package ua.com.foxminded.university;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import ua.com.foxminded.university.handlers.ClassroomsMenu;
import ua.com.foxminded.university.handlers.GroupsMenu;
import ua.com.foxminded.university.handlers.LecturesMenu;
import ua.com.foxminded.university.handlers.SubjectsMenu;
import ua.com.foxminded.university.handlers.TeachersMenu;
import ua.com.foxminded.university.model.University;

public class Menu {

    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String CR = System.lineSeparator();
    public static final String FORMAT_DIVIDER = "----------------" + CR;
    private static final String MAIN_MENU_TEXT = "Main menu" + CR
	    + "1. View full university contents" + CR
	    + "2. Manage teachers" + CR
	    + "3. Manage groups" + CR
	    + "4. Manage subjects" + CR
	    + "5. Manage lectures" + CR
	    + "6. Manage classrooms" + CR
	    + "7. Rename university" + CR
	    + "Enter choice or 0 to quit:";

    private static final String CRUD_MENU_TEXT = CR
	    + "1. Create" + CR
	    + "2. Read (all)" + CR
	    + "3. Update" + CR
	    + "4. Delete" + CR
	    + "Enter choice or 0 to return:";

    public static Scanner scanner = new Scanner(System.in);
    private University university;

    public Menu(University university) {
	this.university = university;
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
	    System.out.print(university.toString());
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
	    university.getTeachers().add(TeachersMenu.createTeacher(university));
	    start(2);
	    break;
	case 2:
	    System.out.println(TeachersMenu.getStringOfTeachers(university.getTeachers()));
	    start(2);
	    break;
	case 3:
	    TeachersMenu.updateTeacher(university);
	    start(2);
	    break;
	case 4:
	    TeachersMenu.deleteTeacher(university);
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
	    university.getGroups().add(GroupsMenu.createGroup(university));
	    start(3);
	    break;
	case 2:
	    System.out.println(GroupsMenu.getStringOfGroups(university.getGroups()));
	    start(3);
	    break;
	case 3:
	    GroupsMenu.updateGroup(university);
	    start(3);
	    break;
	case 4:
	    GroupsMenu.deleteGroup(university);
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
	    university.getSubjects().add(SubjectsMenu.createSubject(university));
	    start(4);
	    break;
	case 2:
	    System.out.println(SubjectsMenu.getStringOfSubjects(university.getSubjects()));
	    start(4);
	    break;
	case 3:
	    SubjectsMenu.updateSubject(university);
	    start(4);
	    break;
	case 4:
	    SubjectsMenu.deleteSubject(university);
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
	    university.getLectures().add(LecturesMenu.createLecture(university));
	    start(5);
	    break;
	case 2:
	    System.out.println(LecturesMenu.getStringOfLectures(university.getLectures()));
	    start(5);
	    break;
	case 3:
	    LecturesMenu.updateLecture(university);
	    start(5);
	    break;
	case 4:
	    LecturesMenu.deleteLecture(university);
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
	    university.getClassrooms().add(ClassroomsMenu.createClassroom(university));
	    start(6);
	    break;
	case 2:
	    System.out.println(ClassroomsMenu.getStringOfClassrooms(university.getClassrooms()));
	    start(6);
	    break;
	case 3:
	    ClassroomsMenu.updateClassroom(university);
	    start(6);
	    break;
	case 4:
	    ClassroomsMenu.deleteClassroom(university);
	    start(6);
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
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
	LocalDate result = null;

	Boolean correctEntry = false;
	while (!correctEntry) {
	    try {
		String line = scanner.nextLine();
		correctEntry = true;
		result = LocalDate.parse(line, formatter);
	    } catch (Exception e) {
		e.printStackTrace();
		correctEntry = false;
	    }
	}
	return result;
    }

    public static LocalTime getTimeFromScanner() {
	DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
	LocalTime result = null;

	Boolean correctEntry = false;
	while (!correctEntry) {
	    try {
		String line = scanner.nextLine();
		correctEntry = true;
		result = LocalTime.parse(line, formatter);
	    } catch (Exception e) {
		e.printStackTrace();
		correctEntry = false;
	    }
	}
	return result;
    }
}
