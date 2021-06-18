package ua.com.foxminded.university.menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

import ua.com.foxminded.university.dao.jdbc.JdbcLectureDAO;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.TimeRange;
import static ua.com.foxminded.university.Menu.*;

@Component
public class LecturesMenu {

    @Autowired
    private GroupsMenu groupsMenu;
    @Autowired
    private SubjectsMenu subjectsMenu;
    @Autowired
    private TeachersMenu teachersMenu;
    @Autowired
    private ClassroomsMenu classroomsMenu;
    @Autowired
    private JdbcLectureDAO jdbcLectureDAO;

    public String getStringOfLectures(List<Lecture> lectures) {
	StringBuilder result = new StringBuilder();
	lectures.sort(Comparator.comparing(Lecture::getId));
	for (Lecture lecture : lectures) {
	    result.append(lecture.getId()).append(". " + getStringFromLecture(lecture));
	}
	return result.toString();
    }

    public String getStringFromLecture(Lecture lecture) {
	StringBuilder result = new StringBuilder();

	result.append("Lecture on " + lecture.getSubject().getName() + " will take place on " + lecture.getDate() + ", from "
		+ lecture.getTime().getStartTime() + " to " + lecture.getTime().getEndTime() + "." + CR);
	result.append("Read by " + lecture.getTeacher().getFirstName() + " " + lecture.getTeacher().getLastName() + " in "
		+ lecture.getClassroom().getName() + "." + CR);

	List<Group> groups = lecture.getGroups();

	result.append("Groups to attend:" + CR + groupsMenu.getStringOfGroups(groups));
	return result.toString();
    }

    public void addLecture() {
	// TODO Auto-generated method stub

    }

    public void printLectures() {
	System.out.println(getStringOfLectures(jdbcLectureDAO.findAll()));
    }

    public void updateLecture() {
	// TODO Auto-generated method stub

    }

    public void deleteLecture() {
	// TODO Auto-generated method stub

    }

//    public Lecture createLecture() {
//	System.out.print("Lecture date: ");
//	LocalDate date = getDateFromScanner();
//
//	TimeRange timeRange = null;
//	while (isNull(timeRange)) {
//	    System.out.print("Lecture begin time: ");
//	    LocalTime startTime = getTimeFromScanner();
//	    System.out.print("Lecture end time: ");
//	    LocalTime endTime = getTimeFromScanner();
//	    if (endTime.isBefore(startTime)) {
//		System.out.println("Wrong entry, try again.");
//	    } else {
//		System.out.println("Success.");
//		timeRange = new TimeRange(startTime, endTime);
//	    }
//	}

//	List<Group> groups = groupsMenu.selectGroups();
//	Subject subject = subjectsMenu.selectSubject();
//	Teacher teacher = teachersMenu.selectTeacher();
//	Classroom classroom = classroomsMenu.selectClassroom();
//
//	return new Lecture(date, timeRange, groups, subject, teacher, classroom);
//    }

//    public void updateLecture() {
//	List<Lecture> lectures = university.getLectures();
//
//	System.out.println("Select a lecture to update: ");
//	System.out.println(getStringOfLectures(lectures));
//	int choice = getIntFromScanner();
//	if (choice > lectures.size()) {
//	    System.out.println("No such lecture, returning...");
//	} else {
//	    lectures.set(choice - 1, createLecture());
//	    System.out.println("Overwrite successful.");
//	}
//    }
//
//    public void deleteLecture() {
//	List<Lecture> lectures = university.getLectures();
//
//	System.out.println("Select a lecture to delete: ");
//	System.out.println(getStringOfLectures(lectures));
//	int choice = getIntFromScanner();
//	if (choice > lectures.size()) {
//	    System.out.println("No such lecture, returning...");
//	} else {
//	    lectures.remove(choice - 1);
//	    System.out.println("Lecture deleted successfully.");
//	}
//    }
}
