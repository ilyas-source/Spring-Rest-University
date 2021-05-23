package ua.com.foxminded.university;

import java.util.Scanner;

import ua.com.foxminded.university.handlers.ClassroomsHandler;
import ua.com.foxminded.university.handlers.GroupsHandler;
import ua.com.foxminded.university.handlers.LecturesHandler;
import ua.com.foxminded.university.handlers.SubjectsHandler;
import ua.com.foxminded.university.handlers.TeachersHandler;
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
	case 7:
	    scanner.nextLine();
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
	    university.getTeachers().add(TeachersHandler.getTeacherFromScanner(university));
	    start(2);
	    break;
	case 2:
	    System.out.println(TeachersHandler.getStringOfTeachers(university.getTeachers()));
	    start(2);
	    break;
	case 3:
	    TeachersHandler.updateATeacher(university);
	    start(2);
	    break;
	case 4:
	    TeachersHandler.DeleteATeacher(university);
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
	    university.getGroups().add(GroupsHandler.getNewGroupFromScanner(university));
	    start(3);
	    break;
	case 2:
	    System.out.println(GroupsHandler.getStringOfGroups(university.getGroups()));
	    start(3);
	    break;
	case 3:
	    GroupsHandler.updateAGroup(university);
	    start(3);
	    break;
	case 4:
	    GroupsHandler.deleteAGroup(university);
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
	    university.getSubjects().add(SubjectsHandler.getNewSubjectFromScanner(university));
	    start(4);
	    break;
	case 2:
	    System.out.println(SubjectsHandler.getStringOfSubjects(university.getSubjects()));
	    start(4);
	    break;
	case 3:
	    SubjectsHandler.updateASubject(university);
	    start(4);
	    break;
	case 4:
	    SubjectsHandler.deleteASubject(university);
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
	    university.getLectures().add(LecturesHandler.getLectureFromScanner(university));
	    start(5);
	    break;
	case 2:
	    System.out.println(LecturesHandler.getStringOfLectures(university.getLectures()));
	    start(5);
	    break;
	case 3:
	    LecturesHandler.updateALecture(university);
	    start(5);
	    break;
	case 4:
	    LecturesHandler.deleteALecture(university);
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
	    university.getClassrooms().add(ClassroomsHandler.getNewClassroomFromScanner(university));
	    start(6);
	    break;
	case 2:
	    System.out.println(ClassroomsHandler.getStringOfClassrooms(university.getClassrooms()));
	    start(6);
	    break;
	case 3:
	    ClassroomsHandler.updateAClassroom(university);
	    start(6);
	    break;
	case 4:
	    ClassroomsHandler.deleteAClassroom(university);
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
	return Integer.parseInt(scanner.nextLine());
    }
}
