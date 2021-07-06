package ua.com.foxminded.university.menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.jdbc.JdbcLectureDao;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Timeslot;

import static ua.com.foxminded.university.Menu.*;

@Component
public class LecturesMenu {

    private GroupsMenu groupsMenu;
    private LectureDao jdbcLectureDao;
    private SubjectsMenu subjectsMenu;
    private TeachersMenu teachersMenu;
    private ClassroomsMenu classroomsMenu;
    private TimeslotsMenu timeslotsMenu;

    public LecturesMenu(GroupsMenu groupsMenu, LectureDao jdbcLectureDao, SubjectsMenu subjectsMenu,
	    TeachersMenu teachersMenu, ClassroomsMenu classroomsMenu, TimeslotsMenu timeslotsMenu) {
	this.groupsMenu = groupsMenu;
	this.jdbcLectureDao = jdbcLectureDao;
	this.subjectsMenu = subjectsMenu;
	this.teachersMenu = teachersMenu;
	this.classroomsMenu = classroomsMenu;
	this.timeslotsMenu = timeslotsMenu;
    }

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
	Subject subject = lecture.getSubject();
	Teacher teacher = lecture.getTeacher();
	Classroom classroom = lecture.getClassroom();

	result.append(
		"Lecture on (" + subject.getId() + ")" + subject.getName() + " will take place on " + lecture.getDate()
			+ ", from "
			+ lecture.getTimeSlot().getBeginTime() + " to " + lecture.getTimeSlot().getEndTime() + " (timeslot #"
			+ lecture.getTimeSlot().getId() + ")" + CR);
	result.append("Read by (" + teacher.getId() + ")" + teacher.getFirstName() + " " + teacher.getLastName() + " in ("
		+ classroom.getId() + ")" + classroom.getName() + "." + CR);

	List<Group> groups = lecture.getGroups();

	result.append("Groups to attend:" + CR + groupsMenu.getStringOfGroups(groups));
	return result.toString();
    }

    public void addLecture() {
	jdbcLectureDao.create(createLecture());
    }

    public void printLectures() {
	System.out.println(getStringOfLectures(jdbcLectureDao.findAll()));
    }

    public Lecture createLecture() {
	System.out.print("Lecture date: ");
	LocalDate date = getDateFromScanner();

	Timeslot timeslot = timeslotsMenu.selectTimeslot();
	List<Group> groups = groupsMenu.selectGroups();
	Subject subject = subjectsMenu.selectSubject();
	Teacher teacher = teachersMenu.selectTeacher();
	Classroom classroom = classroomsMenu.selectClassroom();

	return Lecture.builder().date(date).subject(subject).timeslot(timeslot)
		.groups(groups).teacher(teacher).classroom(classroom)
		.build();
    }

    public Lecture selectLecture() {
	List<Lecture> lectures = jdbcLectureDao.findAll();
	Lecture result = null;

	while (isNull(result)) {
	    System.out.println("Select lecture: ");
	    System.out.print(getStringOfLectures(lectures));
	    int choice = getIntFromScanner();
	    Optional<Lecture> selectedLecture = jdbcLectureDao.findById(choice);
	    if (isNull(selectedLecture.isEmpty())) {
		System.out.println("No such lecture.");
	    } else {
		result = selectedLecture.get();
		System.out.println("Success.");
	    }
	}
	return result;
    }

    public void updateLecture() {
	Lecture oldLecture = selectLecture();
	Lecture newLecture = createLecture();
	newLecture.setId(oldLecture.getId());
	jdbcLectureDao.update(newLecture);
	System.out.println("Overwrite successful.");
    }

    public void deleteLecture() {
	jdbcLectureDao.delete(selectLecture().getId());
	System.out.println("Lecture deleted successfully.");
    }
}
