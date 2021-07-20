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
	var canCreate = isClassroomCapacityEnough(lecture)
		&& isClassroomAvailable(lecture)
		&& isNotHoliday(lecture)
		&& isTeacherAvailable(lecture)
		&& isTeacherWorking(lecture)
		&& canTeacherTeach(lecture)
		&& canAllGroupsAttend(lecture)
		&& !isWeekEnd(lecture);
	if (canCreate) {
	    lectureDao.create(lecture);
	}
    }

    private boolean isWeekEnd(Lecture lecture) {
	var dayOfWeek = DayOfWeek.of(lecture.getDate().get(ChronoField.DAY_OF_WEEK));

	return (dayOfWeek == DayOfWeek.SATURDAY) || (dayOfWeek == DayOfWeek.SUNDAY);
    }

    private boolean isClassroomAvailable(Lecture lecture) {
	return lectureDao.findByDateTimeClassroom(lecture.getDate(), lecture.getTimeslot(), lecture.getClassroom())
		.isEmpty();

    }

    private boolean canAllGroupsAttend(Lecture lecture) {
	List<Lecture> lecturesOnThisDateAndTime = lectureDao.findByDateTime(lecture.getDate(), lecture.getTimeslot());
	return lecturesOnThisDateAndTime.stream()
		.map(Lecture::getGroups)
		.flatMap(List::stream)
		.noneMatch(lecture.getGroups()::contains);
    }

    private boolean canTeacherTeach(Lecture lecture) {
	var teacher = lecture.getTeacher();
	var subject = lecture.getSubject();
	return teacher.getSubjects().contains(subject);
    }

    private boolean isTeacherWorking(Lecture lecture) {
	return lecture.getTeacher()
		.getVacations()
		.stream()
		.noneMatch(v -> isDayWithinVacation(lecture.getDate(), v));
    }

    private boolean isDayWithinVacation(LocalDate date, Vacation vacation) {
	return !date.isBefore(vacation.getStartDate()) && !date.isAfter(vacation.getEndDate());
    }

    private boolean isTeacherAvailable(Lecture lecture) {
	return lectureDao.findByDateTimeTeacher(lecture.getDate(), lecture.getTimeslot(), lecture.getTeacher())
		.isEmpty();
    }

    private boolean isNotHoliday(Lecture lecture) {
	return holidayDao.findByDate(lecture.getDate()).isEmpty();
    }

    private boolean isClassroomCapacityEnough(Lecture lecture) {
	int requiredCapacity = countStudentsInLecture(lecture);
	return lecture.getClassroom().getCapacity() >= requiredCapacity;
    }

    public List<Lecture> findAll() {
	return lectureDao.findAll();
    }

    public Optional<Lecture> findById(int id) {
	return lectureDao.findById(id);
    }

    public void update(Lecture lecture) {
	logger.debug("Updating lecture: {} ", lecture);
	var canUpdate = isClassroomCapacityEnough(lecture)
		&& isClassroomAvailable(lecture)
		&& isNotHoliday(lecture)
		&& isTeacherAvailable(lecture)
		&& isTeacherWorking(lecture)
		&& canTeacherTeach(lecture)
		&& canAllGroupsAttend(lecture)
		&& !isWeekEnd(lecture);
	if (canUpdate) {
	    lectureDao.update(lecture);
	}
    }

    public int countStudentsInLecture(Lecture lecture) {
	return lecture.getGroups()
		.stream()
		.mapToInt(studentDao::countInGroup)
		.sum();
    }

    public void delete(int id) {
	logger.debug("Deleting lecture by id: {} ", id);
	if (idExists(id)) {
	    lectureDao.delete(id);
	}
    }

    private boolean idExists(int id) {
	return lectureDao.findById(id).isPresent();
    }
}
