package ua.com.foxminded.university.menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.TimeRange;
import ua.com.foxminded.university.model.University;
import static ua.com.foxminded.university.Menu.*;

public class LecturesMenu {

    private GroupsMenu groupsMenu;
    private SubjectsMenu subjectsMenu;
    private TeachersMenu teachersMenu;
    private ClassroomsMenu classroomsMenu;

    public LecturesMenu() {
	this.groupsMenu = new GroupsMenu();
	this.subjectsMenu = new SubjectsMenu();
	this.teachersMenu = new TeachersMenu();
	this.classroomsMenu = new ClassroomsMenu();
    }

    public String getStringOfLectures(List<Lecture> lectures) {
	StringBuilder result = new StringBuilder();
	for (Lecture lecture : lectures) {
	    result.append(lectures.indexOf(lecture) + 1).append(". " + lecture);
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

    public Lecture createLecture(University university) {
	LocalTime startTime = null;
	LocalTime endTime = null;

	System.out.print("Lecture date: ");
	LocalDate date = getDateFromScanner();

	Boolean correctEntry = false;
	while (!correctEntry) {
	    System.out.print("Lecture begin time: ");
	    startTime = getTimeFromScanner();
	    System.out.print("Lecture end time: ");
	    endTime = getTimeFromScanner();
	    if (endTime.isBefore(startTime)) {
		System.out.println("Wrong entry, try again.");
	    } else {
		correctEntry = true;
		System.out.println("Success.");
	    }
	}

	List<Group> groups = groupsMenu.selectGroups(university);
	Subject subject = subjectsMenu.selectSubject(university);
	Teacher teacher = teachersMenu.selectTeacher(university);
	Classroom classroom = classroomsMenu.selectClassroom(university);

	return new Lecture(date, new TimeRange(startTime, endTime), groups, subject, teacher, classroom);
    }

    public void updateLecture(University university) {

	List<Lecture> lectures = university.getLectures();

	System.out.println("Select a lecture to update: ");
	System.out.println(getStringOfLectures(lectures));
	int choice = getIntFromScanner() - 1;
	if (choice > lectures.size()) {
	    System.out.println("No such lecture, returning...");
	} else {
	    lectures.set(choice, createLecture(university));
	    System.out.println("Overwrite successful.");
	}
    }

    public void deleteLecture(University university) {
	List<Lecture> lectures = university.getLectures();

	System.out.println("Select a lecture to delete: ");
	System.out.println(getStringOfLectures(lectures));
	int choice = getIntFromScanner() - 1;
	if (choice > lectures.size()) {
	    System.out.println("No such lecture, returning...");
	} else {
	    lectures.remove(choice);
	    System.out.println("Lecture deleted successfully.");
	}
    }
}
