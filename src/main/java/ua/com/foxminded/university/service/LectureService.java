package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.exception.*;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class LectureService {

    private static final Logger logger = LoggerFactory.getLogger(LectureService.class);

    private LectureDao lectureDao;
    private HolidayDao holidayDao;
    private StudentDao studentDao;
    private TeacherService teacherService;

    public LectureService(LectureDao lectureDao, HolidayDao holidayDao, StudentDao studentDao, TeacherService teacherService) {
        this.lectureDao = lectureDao;
        this.holidayDao = holidayDao;
        this.studentDao = studentDao;
        this.teacherService = teacherService;
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

    public Lecture getById(int id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find lecture by id " + id));
    }

    public void update(Lecture lecture) {
        logger.debug("Updating lecture: {} ", lecture);
        verifyAllDataIsCorrect(lecture);
        lectureDao.update(lecture);
    }

    public void delete(int id) {
        logger.debug("Deleting lecture by id: {} ", id);
        verifyIdExists(id);
        lectureDao.delete(getById(id));
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
        if (lectureDao.findByDateTimeClassroom(lecture.getDate(), lecture.getTimeslot(), lecture.getClassroom())
                .filter(l -> l.getId() != lecture.getId())
                .isPresent()) {
            throw new ClassroomOccupiedException(
                    String.format("Classroom %s is occupied at this day and time", lecture.getClassroom().getName()));
        }
    }

    private void verifyAllGroupsCanAttend(Lecture lecture) {
        if (lectureDao.findByDateTime(lecture.getDate(), lecture.getTimeslot())
                .stream()
                .filter(l -> l.getId() != lecture.getId())
                .map(Lecture::getGroups)
                .flatMap(Set::stream)
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
                    teacher.getFirstName(), teacher.getLastName()));
        }
    }

    private boolean isDayWithinVacation(LocalDate date, Vacation vacation) {
        return !date.isBefore(vacation.getStartDate()) && !date.isAfter(vacation.getEndDate());
    }

    private void verifyTeacherIsNotBusy(Lecture lecture) {
        var teacher = lecture.getTeacher();
        if (lectureDao.findByDateTimeTeacher(lecture.getDate(), lecture.getTimeslot(), teacher)
                .filter(l -> l.getId() != lecture.getId())
                .isPresent()) {
            throw new TeacherBusyException(
                    String.format("Teacher %s %s will be reading another lecture",
                            teacher.getFirstName(), teacher.getLastName()));
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
        if (lectureDao.findById(id).isEmpty()) {
            throw new EntityNotFoundException(String.format("Lecture id:%s not found, nothing to delete", id));
        }
    }

    public List<Lecture> findByTeacherAndPeriod(Teacher teacher, LocalDate start, LocalDate end) {
        logger.debug("Retrieving lectures for teacher {} {} and period {}-{}", teacher.getFirstName(), teacher.getLastName(), start, end);
        return lectureDao.findByTeacherAndPeriod(teacher, start, end);
    }

    public List<Lecture> findByStudentAndPeriod(Student student, LocalDate start, LocalDate end) {
        logger.debug("Retrieving lectures for student {} {} and period {}-{}", student.getFirstName(), student.getLastName(), start, end);
        return lectureDao.findByStudentAndPeriod(student, start, end);
    }

    @Transactional
    public void replaceTeacher(Teacher teacher, LocalDate start, LocalDate end) {
        List<Lecture> lectures = lectureDao.findByTeacherAndPeriod(teacher, start, end);
        logger.debug("Found {} lectures for this teacher and dates: {}", lectures.size(), lectures);

        for (Lecture lecture : lectures) {
            logger.debug("Trying to replace teacher in {}", lecture);

            List<Teacher> replacementTeachers = teacherService.getReplacementTeachers(lecture);
            logger.debug("Found {} suitable teachers", replacementTeachers.size());

            if (replacementTeachers.size() == 0) {
                throw new CannotReplaceTeacherException(String.format("Can't replace teacher in %s: no suitable teachers found", lecture));
            }
            Teacher goodTeacher = replacementTeachers.get(0);
            logger.debug("Found good candidate: {} {}", goodTeacher.getFirstName(), goodTeacher.getLastName());
            lecture.setTeacher(goodTeacher);
            lectureDao.update(lecture);
        }
    }
}
