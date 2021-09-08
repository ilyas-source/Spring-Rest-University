package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.exception.*;
import ua.com.foxminded.university.model.*;

import java.util.*;
import java.util.stream.Collectors;

@PropertySource("classpath:university.properties")
@Service
public class TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    private TeacherDao teacherDao;
    private LectureDao lectureDao;
    private VacationService vacationService;

    @Value("#{${teacher.vacationdays}}")
    public Map<Degree, Integer> vacationDays = new EnumMap<>(Degree.class);

    public TeacherService(TeacherDao jdbcTeacherDao, LectureDao lectureDao, VacationService vacationService) {
        this.teacherDao = jdbcTeacherDao;
        this.lectureDao = lectureDao;
        this.vacationService = vacationService;
    }

    public void create(Teacher teacher) {
        logger.debug("Creating a new teacher: {} ", teacher);
        verifyIsUnique(teacher);
        verifyhasEnoughVacationDays(teacher);
        teacherDao.create(teacher);
    }

    public List<Teacher> findAll(Pageable pageable) {
        return teacherDao.findAll(pageable);
    }

    public Optional<Teacher> findById(int id) {
        return teacherDao.findById(id);
    }

    public void update(Teacher teacher) {
        logger.debug("Updating teacher: {} ", teacher);
        verifyCanTeachScheduledLectures(teacher);
        verifyhasEnoughVacationDays(teacher);
        teacherDao.update(teacher);
    }

    public void delete(int id) {
        logger.debug("Deleting teacher by id: {} ", id);
        var teacher = teacherDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Teacher id:%s not found, nothing to delete", id)));
        verifyHasNoLectures(teacher);
        teacherDao.delete(id);
    }

    private void verifyhasEnoughVacationDays(Teacher teacher) {
        int allowedDays = vacationDays.get(teacher.getDegree());
        List<Vacation> vacations = teacher.getVacations();
        Map<Integer, Long> daysCountByYears = vacationService.countDaysByYears(vacations);
        if (teacher.getVacations().isEmpty()) {
            return;
        }
        long maxDays = daysCountByYears.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get()
                .getValue();
        if (maxDays > allowedDays) {
            throw new VacationInsufficientDaysException(
                    String.format("Teacher has maximum %s vacation days per year, can't assign %s days", allowedDays, maxDays));
        }
    }

    public void verifyIsUnique(Teacher teacher) {
        if (teacherDao.findByNameAndEmail(teacher.getFirstName(), teacher.getLastName(), teacher.getEmail()).isPresent()) {
            throw new EntityNotUniqueException(String.format("Teacher %s %s with email %s already exists, can't create duplicate",
                    teacher.getFirstName(), teacher.getLastName(), teacher.getEmail()));
        }
    }

    private void verifyCanTeachScheduledLectures(Teacher teacher) {
        List<Subject> requiredSubjects = lectureDao.findByTeacher(teacher)
                .stream()
                .map(Lecture::getSubject)
                .collect(Collectors.toList());
        if (!teacher.getSubjects().containsAll(requiredSubjects)) {
            throw new TeacherCannotTeachSubject(
                    String.format("Updated teacher %s %s can't teach scheduled lecture(s)", teacher.getFirstName(),
                            teacher.getLastName()));
        }
    }

    private void verifyHasNoLectures(Teacher teacher) {
        if (!lectureDao.findByTeacher(teacher).isEmpty()) {
            throw new TeacherBusyException(String.format("Teacher %s %s has scheduled lecture(s), can't delete",
                    teacher.getFirstName(), teacher.getLastName()));
        }
    }
}
