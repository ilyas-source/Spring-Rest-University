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

    University university;

    public UniversityMenu(University university) {
	this.university = university;
	this.teachersMenu = new TeachersMenu(university);
	this.studentsMenu = new StudentsMenu(university);
	this.groupsMenu = new GroupsMenu(university);
	this.lecturesMenu = new LecturesMenu(university);
	this.holidaysMenu = new HolidaysMenu(university);
	this.classroomsMenu = new ClassroomsMenu(university);
	this.subjectsMenu = new SubjectsMenu(university);
    }

    public void printUniversity() {
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
