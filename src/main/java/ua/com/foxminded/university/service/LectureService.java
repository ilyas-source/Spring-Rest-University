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
	boolean canCreate = classroomCapacityIsEnough(lecture);
	canCreate = canCreate && classroomIsAvailable(lecture);
	canCreate = canCreate && isNotHoliday(lecture);
	canCreate = canCreate && teacherIsNotBusy(lecture);
	canCreate = canCreate && teacherIsWorking(lecture);
	canCreate = canCreate && teacherCanTeach(lecture);
	canCreate = canCreate && allGroupsCanAttend(lecture);
	if (canCreate) {
	    lectureDao.create(lecture);
	} else {
	    System.out.println("Can't create lecture");
	}
    }

    private boolean classroomIsAvailable(Lecture lecture) {
	boolean result = lectureDao.findByDateTimeClassroom(lecture.getDate(), lecture.getTimeslot(), lecture.getClassroom())
		.isEmpty();
	System.out.println("Classroom is available: " + result);
	return result;
    }

    private boolean allGroupsCanAttend(Lecture lecture) {
	List<Lecture> lecturesOnThisDateAndTime = lectureDao.findByDateTime(lecture.getDate(), lecture.getTimeslot());
	boolean result = lecturesOnThisDateAndTime.stream()
		.flatMap(l -> Stream.of(l.getGroups()))
		.flatMap(List::stream)
		.filter(lecture.getGroups()::contains)
		.findFirst()
		.isEmpty();
	System.out.println("All groups can attend: " + result);
	return result;
    }

    private boolean teacherCanTeach(Lecture lecture) {
	Teacher teacher = lecture.getTeacher();
	Subject subject = lecture.getSubject();
	boolean result = teacher.getSubjects().contains(subject);
	System.out.println("Teacher can teach needed subject: " + result);
	return result;
    }

    private boolean teacherIsWorking(Lecture lecture) {
	boolean result = lecture.getTeacher()
		.getVacations()
		.stream()
		.filter(v -> vacationContainsDay(v, lecture.getDate()))
		.findFirst()
		.isEmpty();
	System.out.println("Teacher is not on a vacation: " + result);
	return result;
    }

    private boolean vacationContainsDay(Vacation vacation, LocalDate date) {
	return !date.isBefore(vacation.getStartDate()) && !date.isAfter(vacation.getEndDate());
    }

    private boolean teacherIsNotBusy(Lecture lecture) {
	boolean result = lectureDao.findByDateTimeTeacher(lecture.getDate(), lecture.getTimeslot(), lecture.getTeacher())
		.isEmpty();
	System.out.println("Teacher is not on another lecture: " + result);
	return result;
    }

    private boolean isNotHoliday(Lecture lecture) {
	boolean result = holidayDao.findByDate(lecture.getDate()).isEmpty();
	System.out.println("Lecture is not scheduled to a holiday: " + result);
	return result;
    }

    private boolean classroomCapacityIsEnough(Lecture lecture) {
	int requiredCapacity = countStudentsInLecture(lecture);
	boolean result = lecture.getClassroom().getCapacity() >= requiredCapacity;
	System.out.println("Classroom is big enough: " + result);
	return result;
    }

    public List<Lecture> findAll() {
	return lectureDao.findAll();
    }

    public Optional<Lecture> findById(int choice) {
	return lectureDao.findById(choice);
    }

    public void update(Lecture lecture) {
	boolean canUpdate = classroomCapacityIsEnough(lecture);
	canUpdate = canUpdate && classroomIsAvailable(lecture);
	canUpdate = canUpdate && isNotHoliday(lecture);
	canUpdate = canUpdate && teacherIsNotBusy(lecture);
	canUpdate = canUpdate && teacherIsWorking(lecture);
	canUpdate = canUpdate && teacherCanTeach(lecture);
	canUpdate = canUpdate && allGroupsCanAttend(lecture);

	if (canUpdate) {
	    lectureDao.update(lecture);
	} else {
	    System.out.println("Can't create lecture");
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
	} else {
	    System.out.println("Can't delete lecture");
	}
    }

    private boolean idExists(int id) {
	return lectureDao.findById(id).isPresent();
    }
}
