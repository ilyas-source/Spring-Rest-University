package ua.com.foxminded.university.menu;

import static java.util.Objects.isNull;
import static ua.com.foxminded.university.Menu.CR;
import static ua.com.foxminded.university.Menu.getDateFromScanner;
import static ua.com.foxminded.university.Menu.getIntFromScanner;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Timeslot;
import ua.com.foxminded.university.service.LectureService;

@Component
public class LecturesMenu {

    private GroupsMenu groupsMenu;
    private LectureService lectureService;
    private SubjectsMenu subjectsMenu;
    private TeachersMenu teachersMenu;
    private ClassroomsMenu classroomsMenu;
    private TimeslotsMenu timeslotsMenu;

    public LecturesMenu(GroupsMenu groupsMenu, LectureService lectureService, SubjectsMenu subjectsMenu,
	    TeachersMenu teachersMenu, ClassroomsMenu classroomsMenu, TimeslotsMenu timeslotsMenu) {
	this.groupsMenu = groupsMenu;
	this.lectureService = lectureService;
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
			+ lecture.getTimeslot().getBeginTime() + " to " + lecture.getTimeslot().getEndTime() + " (timeslot #"
			+ lecture.getTimeslot().getId() + ")" + CR);
	result.append("Read by (" + teacher.getId() + ")" + teacher.getFirstName() + " " + teacher.getLastName() + " in ("
		+ classroom.getId() + ")" + classroom.getName() + "." + CR);

	List<Group> groups = lecture.getGroups();

	result.append("Groups to attend:" + CR + groupsMenu.getStringOfGroups(groups));
	return result.toString();
    }

    public void addLecture() {
	lectureService.create(createLecture());
    }

    public void printLectures() {
	System.out.println(getStringOfLectures(lectureService.findAll()));
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
	List<Lecture> lectures = lectureService.findAll();
	Lecture result = null;

	while (isNull(result)) {
	    System.out.println("Select lecture: ");
	    System.out.print(getStringOfLectures(lectures));
	    int choice = getIntFromScanner();
	    Optional<Lecture> selectedLecture = lectureService.findById(choice);
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
	lectureService.update(newLecture);
	System.out.println("Overwrite successful.");
    }

    public void deleteLecture() {
	lectureService.delete(selectLecture().getId());
	System.out.println("Lecture deleted successfully.");
    }
}
