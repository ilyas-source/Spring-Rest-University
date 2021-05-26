package ua.com.foxminded.university.menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static java.util.Objects.isNull;

import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.TimeRange;
import ua.com.foxminded.university.model.University;
import static ua.com.foxminded.university.Menu.*;

public class LecturesMenu {

    private University university;
    private GroupsMenu groupsMenu;
    private SubjectsMenu subjectsMenu;
    private TeachersMenu teachersMenu;
    private ClassroomsMenu classroomsMenu;

    public LecturesMenu(University university) {
	this.university = university;
	this.groupsMenu = new GroupsMenu(university);
	this.subjectsMenu = new SubjectsMenu(university);
	this.teachersMenu = new TeachersMenu(university);
	this.classroomsMenu = new ClassroomsMenu(university);
    }

    public String getStringOfLectures(List<Lecture> lectures) {
	StringBuilder result = new StringBuilder();
	for (Lecture lecture : lectures) {
	    result.append(lectures.indexOf(lecture) + 1).append(". " + getStringOfLecture(lecture));
	}
	return result.toString();
    }

    public String getStringOfLecture(Lecture lecture) {

	StringBuilder result = new StringBuilder();

	result.append("Lecture on " + lecture.getSubject().getName() + " will take place on " + lecture.getDate() + ", from "
		+ lecture.getTime().getStartTime() + " to " + lecture.getTime().getEndTime() + "." + CR);
	result.append("Read by " + lecture.getTeacher().getFirstName() + " " + lecture.getTeacher().getLastName() + " in "
		+ lecture.getClassroom().getName() + "." + CR);
	result.append("Groups to attend:" + CR + groupsMenu.getStringOfGroups(lecture.getGroups()));
	return result.toString();
    }

    public Lecture createLecture() {
	System.out.print("Lecture date: ");
	LocalDate date = getDateFromScanner();

	TimeRange timeRange = null;
	while (isNull(timeRange)) {
	    System.out.print("Lecture begin time: ");
	    LocalTime startTime = getTimeFromScanner();
	    System.out.print("Lecture end time: ");
	    LocalTime endTime = getTimeFromScanner();
	    if (endTime.isBefore(startTime)) {
		System.out.println("Wrong entry, try again.");
	    } else {
		System.out.println("Success.");
		timeRange = new TimeRange(startTime, endTime);
	    }
	}

	List<Group> groups = groupsMenu.selectGroups();
	Subject subject = subjectsMenu.selectSubject();
	Teacher teacher = teachersMenu.selectTeacher();
	Classroom classroom = classroomsMenu.selectClassroom();

	return new Lecture(date, timeRange, groups, subject, teacher, classroom);
    }

    public void updateLecture() {
	List<Lecture> lectures = university.getLectures();

	System.out.println("Select a lecture to update: ");
	System.out.println(getStringOfLectures(lectures));
	int choice = getIntFromScanner();
	if (choice > lectures.size()) {
	    System.out.println("No such lecture, returning...");
	} else {
	    lectures.set(choice - 1, createLecture());
	    System.out.println("Overwrite successful.");
	}
    }

    public void deleteLecture() {
	List<Lecture> lectures = university.getLectures();

	System.out.println("Select a lecture to delete: ");
	System.out.println(getStringOfLectures(lectures));
	int choice = getIntFromScanner();
	if (choice > lectures.size()) {
	    System.out.println("No such lecture, returning...");
	} else {
	    lectures.remove(choice - 1);
	    System.out.println("Lecture deleted successfully.");
	}
    }
}
