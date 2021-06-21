package ua.com.foxminded.university.menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import static java.util.Objects.isNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private JdbcLectureDAO jdbcLectureDAO;
    @Autowired
    private SubjectsMenu subjectsMenu;
    @Autowired
    private TeachersMenu teachersMenu;
    @Autowired
    private ClassroomsMenu classroomsMenu;

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
	jdbcLectureDAO.addToDb(createLecture());
    }

    public void printLectures() {
	System.out.println(getStringOfLectures(jdbcLectureDAO.findAll()));
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

    public Lecture selectLecture() {
	List<Lecture> lectures = jdbcLectureDAO.findAll();
	Lecture result = null;

	while (isNull(result)) {
	    System.out.println("Select lecture: ");
	    System.out.print(getStringOfLectures(lectures));
	    int choice = getIntFromScanner();
	    Lecture selected = jdbcLectureDAO.findById(choice).orElse(null);
	    if (isNull(selected)) {
		System.out.println("No such lecture.");
	    } else {
		result = selected;
		System.out.println("Success.");
	    }
	}
	return result;
    }

    public void updateLecture() {
	Lecture oldLecture = selectLecture();
	Lecture newLecture = createLecture();
	newLecture.setId(oldLecture.getId());
	jdbcLectureDAO.update(newLecture);
	System.out.println("Overwrite successful.");
    }

    public void deleteLecture() {
	jdbcLectureDAO.delete(selectLecture().getId());
	System.out.println("Lecture deleted successfully.");
    }
}
