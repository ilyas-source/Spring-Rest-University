package ua.com.foxminded.university;

import java.util.Scanner;

import ua.com.foxminded.university.handlers.TeacherHandler;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.University;

public class Menu {

    private static final String CR = System.lineSeparator();

    private static final String MAIN_MENU_TEXT = "Main menu" + CR
	    + "1. View full university contents" + CR
	    + "2. Manage teachers" + CR
	    + "3. Manage groups" + CR
	    + "4. Manage subjects" + CR
	    + "5. Manage lectures" + CR
	    + "6. Manage classrooms" + CR
	    + "Enter choice or 0 to quit:";

    private static final String CRUD_MENU_TEXT = CR
	    + "1. View all" + CR
	    + "2. Create" + CR
	    + "3. Read" + CR
	    + "4. Update" + CR
	    + "5. Delete" + CR
	    + "Enter choice or 0 to return:";

    Scanner scanner;
    University university;
    TeacherHandler teacherHandler;

    public Menu(University university) {
	scanner = new Scanner(System.in);
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
	    System.exit(0);
//	case 1:
//	    printUniversity();
//	    break;
	case 2:
	    System.out.println("Manage teachers:" + CRUD_MENU_TEXT);
	    menuChoice = readNextInt();
	    manageTeachers(menuChoice);
	    break;
	case 3:
	    System.out.println("Manage groups:" + CRUD_MENU_TEXT);
	    menuChoice = readNextInt();
	    manageGroups(menuChoice);
	    break;
	case 4:
	    System.out.println("Manage subjects:" + CRUD_MENU_TEXT);
	    menuChoice = readNextInt();
	    manageSubjects(menuChoice);
	    break;
	case 5:
	    System.out.println("Manage lectures:" + CRUD_MENU_TEXT);
	    menuChoice = readNextInt();
	    manageLectures(menuChoice);
	    break;
	case 6:
	    System.out.println("Manage classrooms:" + CRUD_MENU_TEXT);
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
	    for (Teacher teacher : university.getTeachers()) {
		System.out.println(teacher);
	    }
	    start(2);
	    break;
	case 2:
	    createTeacher();
	    start(2);
	    break;
	case 3:
	    System.out.println("STUB Teacher viewing");
	    start(2);
	    break;
	case 4:
	    System.out.println("STUB Teacher updating");
	    start(2);
	    break;
	case 5:
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
	    System.out.println("STUB Groups list");
	    start(3);
	    break;
	case 2:
	    System.out.println("STUB Group creation");
	    start(3);
	    break;
	case 3:
	    System.out.println("STUB Group viewing");
	    start(3);
	    break;
	case 4:
	    System.out.println("STUB Group updating");
	    start(3);
	    break;
	case 5:
	    System.out.println("STUB Group deletion");
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

    private int readNextInt() {
	while (!scanner.hasNextInt()) {
	    scanner.next();
	}
	return scanner.nextInt();
    }
}
