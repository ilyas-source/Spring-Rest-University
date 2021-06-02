package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.model.University;

@Component
public class UniversityMenu {

    private University university;
    private TeachersMenu teachersMenu;
    private ClassroomsMenu classroomsMenu;
    private StudentsMenu studentsMenu;
    private LecturesMenu lecturesMenu;
    private HolidaysMenu holidaysMenu;
    private GroupsMenu groupsMenu;
    private SubjectsMenu subjectsMenu;

    public UniversityMenu(University university) {
	this.university = university;
	this.classroomsMenu = new ClassroomsMenu(university);
	this.studentsMenu = new StudentsMenu(university);
	this.lecturesMenu = new LecturesMenu(university);
	this.holidaysMenu = new HolidaysMenu(university);
	this.groupsMenu = new GroupsMenu(university);
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
