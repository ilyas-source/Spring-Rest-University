package ua.com.foxminded.university.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

@Service
public class LectureService {

    private LectureDao lectureDao;
    private HolidayDao holidayDao;
    private TeacherDao teacherDao;

    public LectureService(LectureDao lectureDao, HolidayDao holidayDao, TeacherDao teacherDao) {
	this.lectureDao = lectureDao;
	this.holidayDao = holidayDao;
	this.teacherDao = teacherDao;
    }

    public void create(Lecture lecture) throws Exception {
	verifyClassroomCapacityIsEnough(lecture);
	verifyIsNotHoliday(lecture);
	verifyTeacherIsNotBusy(lecture);
	verifyTeacherIsWorking(lecture);
	verifyTeacherCanTeach(lecture);
	// ��������� ��� �������� �� ������ ������ �������
	// ��������� ��� ��������� �� ������ ������ �������
	// ��������� ��� �� �����������
	lectureDao.create(lecture);
    }

    private void verifyTeacherCanTeach(Lecture lecture) throws Exception {
	Teacher teacher = lecture.getTeacher();
	Subject subject = lecture.getSubject();
	if (!teacher.getSubjects().contains(subject)) {
	    throw new Exception(String.format("%s %s can't teach %s, can't assign lecture", teacher.getFirstName(),
		    teacher.getLastName(), subject.getName()));
	}
    }

    private void verifyTeacherIsWorking(Lecture lecture) throws Exception {
	List<Vacation> vacations = lecture.getTeacher().getVacations();
	boolean teacherIsOnVacation = vacations.stream()
		.filter(v -> vacationContainsDay(v, lecture.getDate()))
		.findFirst()
		.isPresent();
	if (teacherIsOnVacation) {
	    throw new Exception("Teacher is on vacation, can't assign this lecture");
	}
    }

    private boolean vacationContainsDay(Vacation vacation, LocalDate date) {
	if (date.isAfter(vacation.getStartDate().minus(1, ChronoUnit.DAYS)) &&
		date.isBefore(vacation.getEndDate().plus(1, ChronoUnit.DAYS))) {
	    return true;
	}
	return false;
    }

    private void verifyTeacherIsNotBusy(Lecture lecture) throws Exception {
	List<Lecture> thisTeacherLectures = lectureDao.findByTeacher(lecture.getTeacher());
	List<Lecture> thisTeacherLecturesToday = thisTeacherLectures.stream()
		.filter(l -> l.getDate().equals(lecture.getDate()))
		.collect(Collectors.toList());
	boolean teacherHasLectureTodayOnThisTimeslot = thisTeacherLecturesToday.stream()
		.filter(l -> l.getTimeSlot().equals(lecture.getTimeSlot()))
		.findFirst()
		.isPresent();
	if (teacherHasLectureTodayOnThisTimeslot) {
	    throw new Exception("Teacher is busy, can't assign this lecture");
	}
    }

    private void verifyIsNotHoliday(Lecture lecture) throws Exception {
	if (holidayDao.findAll()
		.stream()
		.filter(h -> h.getDate().equals(lecture.getDate()))
		.findFirst()
		.isPresent()) {
	    throw new Exception("Can't schedule lecture to a holiday");
	}
    }

    private void verifyClassroomCapacityIsEnough(Lecture lecture) throws Exception {
	int requiredCapacity = lectureDao.countStudentsInLecture(lecture);
	if (lecture.getClassroom().getCapacity() < requiredCapacity) {
	    throw new Exception(String.format("Required minimum classroom capacity %s, but was %s", requiredCapacity,
		    lecture.getClassroom().getCapacity()));
	}
    }

    public List<Lecture> findAll() {
	return lectureDao.findAll();
    }

    public Optional<Lecture> findById(int choice) {
	return lectureDao.findById(choice);
    }

    public void update(Lecture newLecture) {
	// ��������� ��� ��� �������� ������
	// ��������� ��� �� ��������
	// ��������� ��� ������ �� ����� ������ �������
	// ��������� ��� �������� �� ������ ������ �������
	// ��������� ��� ��������� �� ������ ������ �������
	// ��������� ��� �� �����������
	lectureDao.update(newLecture);
    }

    public void delete(int id) {
	// ��������� ��� ������ ����������
	lectureDao.delete(id);
    }
}
