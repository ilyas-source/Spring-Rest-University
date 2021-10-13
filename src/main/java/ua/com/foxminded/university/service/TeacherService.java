package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
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
        verifyHasEnoughVacationDays(teacher);
        teacherDao.create(teacher);
    }

    public Page<Teacher> findAll(Pageable pageable) {
        logger.debug("Retrieving page {}, size {}, sort {}", pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort());
        Page<Teacher> teachers = teacherDao.findAll(pageable);

        return teachers;
    }

    public List<Teacher> findAll() {
        return teacherDao.findAll();
    }

    public Optional<Teacher> findById(int id) {
        return teacherDao.findById(id);
    }

    public Teacher getById(int id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find teacher by id " + id));
    }

    public void update(Teacher teacher) {
        logger.debug("Updating teacher: {} ", teacher);
        verifyCanTeachScheduledLectures(teacher);
        verifyHasEnoughVacationDays(teacher);
        verifyHasNoIntersectingVacations(teacher);
        teacherDao.update(teacher);
    }

    private void verifyHasNoIntersectingVacations(Teacher teacher) {
        var vacations = teacher.getVacations();
        if (vacations.size() < 2) {
            return;
        }
        for (int i = 0; i < vacations.size(); i++) {
            for (int j = i + 1; j < vacations.size(); j++) {
                if (vacationsIntersect(vacations.get(i), vacations.get(j))) {
                    throw new VacationsIntersectionException(
                            "Teacher has intersecting vacations, can't create/update");
                }
            }
        }
    }

    boolean vacationsIntersect(Vacation v1, Vacation v2) {
        logger.debug("Comparing vacations: {} and {}", v1, v2);
        if (v1.getStartDate().isBefore(v2.getEndDate()) && (v1.getEndDate().isAfter(v2.getStartDate()))) {
            return true;
        }
        if (v1.getStartDate().isEqual(v2.getEndDate())) {
            return true;
        }
        if (v1.getEndDate().isEqual(v2.getStartDate())) {
            return true;
        }
        return false;
    }

    public void delete(int id) {
        logger.debug("Deleting teacher by id: {} ", id);
        verifyHasNoLectures(getById(id));
        teacherDao.delete(id);
    }

    private void verifyHasEnoughVacationDays(Teacher teacher) {
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
                    String.format("Teacher has maximum %s vacation days per year, can't assign %s days", allowedDays,
                            maxDays));
        }
    }

    public void verifyIsUnique(Teacher teacher) {
        if (teacherDao.findByNameAndEmail(teacher.getFirstName(), teacher.getLastName(),
                teacher.getEmail()).isPresent()) {
            throw new EntityNotUniqueException(
                    String.format("Teacher %s %s with email %s already exists, can't create duplicate",
                            teacher.getFirstName(), teacher.getLastName(), teacher.getEmail()));
        }
    }

    private void verifyCanTeachScheduledLectures(Teacher teacher) {
        List<Subject> requiredSubjects = lectureDao.findByTeacher(teacher)
                .stream()
                .map(Lecture::getSubject)
                .collect(Collectors.toList());
        logger.debug("Required subjects for this teacher: {}", requiredSubjects);
        logger.debug("Subject to be updated: {}", teacher.getSubjects());
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


    public List<Teacher> findBySubstring(String substring) {
        return teacherDao.findBySubstring(substring);
    }
}
