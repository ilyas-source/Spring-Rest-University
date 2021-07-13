package ua.com.foxminded.university.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

@Service
public class LectureService {

    private LectureDao lectureDao;
    private HolidayDao holidayDao;
    private StudentDao studentDao;

    public LectureService(LectureDao lectureDao, HolidayDao holidayDao, StudentDao studentDao) {
	this.lectureDao = lectureDao;
	this.holidayDao = holidayDao;
	this.studentDao = studentDao;
    }

    public void create(Lecture lecture) {
	boolean canCreate = isClassroomCapacityEnough(lecture)
		&& isClassroomAvailable(lecture)
		&& isNotHoliday(lecture)
		&& isTeacherAvailable(lecture)
		&& isTeacherWorking(lecture)
		&& canTeacherTeach(lecture)
		&& canAllGroupsAttend(lecture);
	if (canCreate) {
	    lectureDao.create(lecture);
	}
    }

    private boolean isClassroomAvailable(Lecture lecture) {
	return lectureDao.findByDateTimeClassroom(lecture.getDate(), lecture.getTimeslot(), lecture.getClassroom())
		.isEmpty();

    }

    private boolean canAllGroupsAttend(Lecture lecture) {
	List<Lecture> lecturesOnThisDateAndTime = lectureDao.findByDateTime(lecture.getDate(), lecture.getTimeslot());
	return lecturesOnThisDateAndTime.stream()
		.flatMap(l -> Stream.of(l.getGroups()))
		.flatMap(List::stream)
		.filter(lecture.getGroups()::contains)
		.findFirst()
		.isEmpty();
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
		.filter(v -> isDayWithinVacation(lecture.getDate(), v))
		.findFirst()
		.isEmpty();
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

    public Optional<Lecture> findById(int choice) {
	return lectureDao.findById(choice);
    }

    public void update(Lecture lecture) {
	boolean canUpdate = isClassroomCapacityEnough(lecture)
		&& isClassroomAvailable(lecture)
		&& isNotHoliday(lecture)
		&& isTeacherAvailable(lecture)
		&& isTeacherWorking(lecture)
		&& canTeacherTeach(lecture)
		&& canAllGroupsAttend(lecture);
	if (canUpdate) {
	    lectureDao.update(lecture);
	}
    }

    public int countStudentsInLecture(Lecture lecture) {
	return lecture.getGroups()
		.stream()
		.flatMap(g -> Stream.of(studentDao.countInGroup(g)))
		.reduce(0, Integer::sum);
    }

    public void delete(int id) {
	boolean canDelete = idExists(id);
	if (canDelete) {
	    lectureDao.delete(id);
	}
    }

    private boolean idExists(int id) {
	return lectureDao.findById(id).isPresent();
    }
}
