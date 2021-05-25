package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import ua.com.foxminded.university.model.University;

public class UniversityMenu {

    TeachersMenu teachersMenu;
    ClassroomsMenu classroomsMenu;
    StudentsMenu studentsMenu;
    LecturesMenu lecturesMenu;
    HolidaysMenu holidaysMenu;
    GroupsMenu groupsMenu;
    SubjectsMenu subjectsMenu;

    public UniversityMenu() {
	this.teachersMenu = new TeachersMenu();
	this.studentsMenu = new StudentsMenu();
	this.groupsMenu = new GroupsMenu();
	this.lecturesMenu = new LecturesMenu();
	this.holidaysMenu = new HolidaysMenu();
	this.classroomsMenu = new ClassroomsMenu();
	this.subjectsMenu = new SubjectsMenu();
    }

    public void printUniversity(University university) {
	StringBuilder result = new StringBuilder();
	result.append(FORMAT_DIVIDER);
	result.append("University name: " + university.getName() + CR);
	result.append(FORMAT_DIVIDER);
	result.append("Teachers are:" + CR);
	result.append(teachersMenu.getStringOfTeachers(university.getTeachers()));
	result.append(FORMAT_DIVIDER);
	result.append("Students are:" + CR);
	result.append(studentsMenu.getStringOfStudents(university.getStudents()));
	result.append(FORMAT_DIVIDER);
	result.append("Current student groups:" + CR);
	result.append(groupsMenu.getStringOfGroups(university.getGroups()));
	result.append(FORMAT_DIVIDER);
	result.append("Subjects available to students:" + CR);
	result.append(subjectsMenu.getStringOfSubjects(university.getSubjects()));
	result.append(FORMAT_DIVIDER);
	result.append("Classrooms list:" + CR);
	result.append(classroomsMenu.getStringOfClassrooms(university.getClassrooms()));
	result.append(FORMAT_DIVIDER);
	result.append("Scheduled lectures:" + CR);
	result.append(lecturesMenu.getStringOfLectures(university.getLectures()));
	result.append(FORMAT_DIVIDER);
	result.append("Holidays are:" + CR);
	result.append(holidaysMenu.getStringOfHolidays(university.getHolidays()));
	result.append(FORMAT_DIVIDER);
	System.out.println(result.toString());
    }
}
