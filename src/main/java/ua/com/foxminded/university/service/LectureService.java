package ua.com.foxminded.university.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.exception.ClassroomInvalidCapacityException;
import ua.com.foxminded.university.exception.ClassroomOccupiedException;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.GroupBusyException;
import ua.com.foxminded.university.exception.LectureOnHolidayException;
import ua.com.foxminded.university.exception.LectureOnWeekendException;
import ua.com.foxminded.university.exception.TeacherBusyException;
import ua.com.foxminded.university.exception.TeacherCannotTeachSubject;
import ua.com.foxminded.university.exception.TeacherOnVacationException;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Vacation;

@Service
public class LectureService {

    private static final Logger logger = LoggerFactory.getLogger(LectureService.class);

    private LectureDao lectureDao;
    private HolidayDao holidayDao;
    private StudentDao studentDao;

    public LectureService(LectureDao lectureDao, HolidayDao holidayDao, StudentDao studentDao) {
	this.lectureDao = lectureDao;
	this.holidayDao = holidayDao;
	this.studentDao = studentDao;
    }

    public void create(Lecture lecture) {
	logger.debug("Creating a new lecture: {} ", lecture);
	verifyAllDataIsCorrect(lecture);
	lectureDao.create(lecture);
    }

    public List<Lecture> findAll() {
	return lectureDao.findAll();
    }

    public Optional<Lecture> findById(int id) {
	return lectureDao.findById(id);
    }

    public void update(Lecture lecture) {
	logger.debug("Updating lecture: {} ", lecture);
	verifyAllDataIsCorrect(lecture);
	lectureDao.update(lecture);
    }

    public void delete(int id) {
	logger.debug("Deleting lecture by id: {} ", id);
	verifyIdExists(id);
	lectureDao.delete(id);
    }

    private void verifyAllDataIsCorrect(Lecture lecture) {
	verifyClassroomCapacityIsEnough(lecture);
	verifyClassroomIsAvailable(lecture);
	verifyIsNotHoliday(lecture);
	verifyTeacherIsNotBusy(lecture);
	verifyTeacherIsWorking(lecture);
	verifyTeacherCanTeachSubject(lecture);
	verifyAllGroupsCanAttend(lecture);
	verifyIsNotWeekEnd(lecture);
    }

    private void verifyIsNotWeekEnd(Lecture lecture) {
	var dayOfWeek = DayOfWeek.of(lecture.getDate().get(ChronoField.DAY_OF_WEEK));
	if ((dayOfWeek == DayOfWeek.SATURDAY) || (dayOfWeek == DayOfWeek.SUNDAY)) {
	    throw new LectureOnWeekendException("Can't schedule lecture to a weekend");
	}
    }

    private void verifyClassroomIsAvailable(Lecture lecture) {
	var classroom = lecture.getClassroom();
	if (!lectureDao.findByDateTimeClassroom(lecture.getDate(), lecture.getTimeslot(), classroom)
		.isEmpty()) {
	    throw new ClassroomOccupiedException(
		    String.format("Classroom %s is occupied at this day and time", classroom.getName()));
	}
    }

    private void verifyAllGroupsCanAttend(Lecture lecture) {
	List<Lecture> lecturesOnThisDateAndTime = lectureDao.findByDateTime(lecture.getDate(), lecture.getTimeslot());
	if (lecturesOnThisDateAndTime.stream()
		.map(Lecture::getGroups)
		.flatMap(List::stream)
		.anyMatch(lecture.getGroups()::contains)) {
	    throw new GroupBusyException("Group(s) will be attending another lecture");
	}
    }

    private void verifyTeacherCanTeachSubject(Lecture lecture) {
	var teacher = lecture.getTeacher();
	var subject = lecture.getSubject();
	if (!teacher.getSubjects().contains(subject)) {
	    throw new TeacherCannotTeachSubject(String.format("Teacher %s %s can't teach %s", teacher.getFirstName(),
		    teacher.getLastName(), subject.getName()));
	}
    }

    private void verifyTeacherIsWorking(Lecture lecture) {
	var teacher = lecture.getTeacher();
	if (teacher.getVacations()
		.stream()
		.anyMatch(v -> isDayWithinVacation(lecture.getDate(), v))) {
	    throw new TeacherOnVacationException(String.format("Teacher %s %s will be on a vacation, can't schedule lecture",
		    teacher.getFirstName(),
		    teacher.getLastName()));
	}
    }

    private boolean isDayWithinVacation(LocalDate date, Vacation vacation) {
	return !date.isBefore(vacation.getStartDate()) && !date.isAfter(vacation.getEndDate());
    }

    private void verifyTeacherIsNotBusy(Lecture lecture) {
	var teacher = lecture.getTeacher();
	if (!lectureDao.findByDateTimeTeacher(lecture.getDate(), lecture.getTimeslot(), teacher)
		.isEmpty()) {
	    throw new TeacherBusyException(String.format("Teacher %s %s will be reading another lecture",
		    teacher.getFirstName(),
		    teacher.getLastName()));
	}
    }

    private void verifyIsNotHoliday(Lecture lecture) {
	if (!holidayDao.findByDate(lecture.getDate()).isEmpty()) {
	    throw new LectureOnHolidayException("Can't schedule lecture to a holiday");
	}
    }

    private void verifyClassroomCapacityIsEnough(Lecture lecture) {
	int requiredCapacity = countStudentsInLecture(lecture);
	if (lecture.getClassroom().getCapacity() < requiredCapacity) {
	    throw new ClassroomInvalidCapacityException(String.format("Classroom too small: required %s, but was %s",
		    requiredCapacity, lecture.getClassroom().getCapacity()));
	}
    }

    public int countStudentsInLecture(Lecture lecture) {
	return lecture.getGroups()
		.stream()
		.mapToInt(studentDao::countInGroup)
		.sum();
    }

    private void verifyIdExists(int id) {
	if (!lectureDao.findById(id).isPresent()) {
	    throw new EntityNotFoundException(String.format("Lecture with id:%s not found, nothing to delete", id));
	}
    }
}
