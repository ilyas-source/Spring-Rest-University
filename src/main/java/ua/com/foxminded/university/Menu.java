package ua.com.foxminded.university;

import java.util.Scanner;

import ua.com.foxminded.university.handlers.ClassRoomsHandler;
import ua.com.foxminded.university.handlers.GroupsHandler;
import ua.com.foxminded.university.handlers.LecturesHandler;
import ua.com.foxminded.university.handlers.SubjectsHandler;
import ua.com.foxminded.university.handlers.TeachersHandler;
import ua.com.foxminded.university.model.University;

public class Menu {

    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String CR = System.lineSeparator();
    public static final String FORMAT_DIVIDER = "----------------" + CR;
    private static final String MAIN_MENU_TEXT = FORMAT_DIVIDER + "Main menu" + CR
	    + "1. View full university contents" + CR
	    + "2. Manage teachers" + CR
	    + "3. Manage groups" + CR
	    + "4. Manage subjects" + CR
	    + "5. Manage lectures" + CR
	    + "6. Manage classrooms" + CR
	    + "Enter choice or 0 to quit:";

    private static final String CRUD_MENU_TEXT = CR
	    + "1. Create" + CR
	    + "2. Read (all)" + CR
	    + "3. Update" + CR
	    + "4. Delete" + CR
	    + "Enter choice or 0 to return:";

    public static Scanner scanner = new Scanner(System.in);
    private University university;
    private TeachersHandler teachersHandler = new TeachersHandler();
    private GroupsHandler groupsHandler = new GroupsHandler();
    private SubjectsHandler subjectsHandler = new SubjectsHandler();
    private LecturesHandler lecturesHandler = new LecturesHandler();
    private ClassRoomsHandler ClassRoomsHandler = new ClassRoomsHandler();

    public Menu(University university) {
	this.university = university;
    }

    public void start(int menuEntryPoint) {

	int menuChoice;

	if (menuEntryPoint == 0) {
	    System.out.println(MAIN_MENU_TEXT);
	    menuChoice = readNextInt();
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
	    menuChoice = readNextInt();
	    manageTeachers(menuChoice);
	    break;
	case 3:
	    System.out.println(FORMAT_DIVIDER + "Manage groups:" + CRUD_MENU_TEXT);
	    menuChoice = readNextInt();
	    manageGroups(menuChoice);
	    break;
	case 4:
	    System.out.println(FORMAT_DIVIDER + "Manage subjects:" + CRUD_MENU_TEXT);
	    menuChoice = readNextInt();
	    manageSubjects(menuChoice);
	    break;
	case 5:
	    System.out.println(FORMAT_DIVIDER + "Manage lectures:" + CRUD_MENU_TEXT);
	    menuChoice = readNextInt();
	    manageLectures(menuChoice);
	    break;
	case 6:
	    System.out.println(FORMAT_DIVIDER + "Manage classrooms:" + CRUD_MENU_TEXT);
	    menuChoice = readNextInt();
	    manageClassrooms(menuChoice);
	    break;
	default:
	    start(0);
	    break;
	}
    }

    private void manageTeachers(int menuChoice) {
	switch (menuChoice) {
	case 1:
	    teachersHandler.addTeacher(university);
	    start(2);
	    break;
	case 2:
	    System.out.println(TeachersHandler.getStringOfTeachers(university.getTeachers()));
	    start(2);
	    break;
	case 3:
	    System.out.println("STUB Teacher updating");
	    start(2);
	    break;
	case 4:
	    System.out.println("STUB Teacher deletion");
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
	    groupsHandler.addGroup(university);
	    start(3);
	    break;
	case 2:
	    System.out.println(GroupsHandler.getStringOfGroups(university.getGroups()));
	    start(3);
	    break;
	case 3:
	    System.out.println("STUB Group updating");
	    start(3);
	    break;
	case 4:
	    System.out.println("STUB Group deletion");
	    start(3);
	    break;
	default:
	    System.out.println("Returning...");
	    start(3);
	    break;
	}
    }

    private void manageSubjects(int menuChoice) {
	switch (menuChoice) {
	case 1:
	    System.out.println("STUB Subjects list");
	    start(4);
	    break;
	case 2:
	    System.out.println("STUB Subject creation");
	    start(4);
	    break;
	case 3:
	    System.out.println("STUB Subject viewing");
	    start(4);
	    break;
	case 4:
	    System.out.println("STUB Subject updating");
	    start(4);
	    break;
	case 5:
	    System.out.println("STUB Subject deletion");
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
	    System.out.println("STUB Lectures list");
	    start(5);
	    break;
	case 2:
	    System.out.println("STUB Lecture creation");
	    start(5);
	    break;
	case 3:
	    System.out.println("STUB Lecture viewing");
	    start(5);
	    break;
	case 4:
	    System.out.println("STUB Lecture updating");
	    start(5);
	    break;
	case 5:
	    System.out.println("STUB Lecture deletion");
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
	    System.out.println("STUB Classrooms list");
	    start(6);
	    break;
	case 2:
	    System.out.println("STUB Classroom creation");
	    start(6);
	    break;
	case 3:
	    System.out.println("STUB Classroom viewing");
	    start(6);
	    break;
	case 4:
	    System.out.println("STUB Classroom updating");
	    start(6);
	    break;
	case 5:
	    System.out.println("STUB Classroom deletion");
	    start(6);
	    break;
	default:
	    System.out.println("Returning...");
	    start(0);
	    break;
	}
    }

    public static int readNextInt() {
	while (!scanner.hasNextInt()) {
	    scanner.next();
	}
	return scanner.nextInt();
    }
}
