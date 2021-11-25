package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.UniversityProperties;
import ua.com.foxminded.university.exception.*;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;
import ua.com.foxminded.university.repository.LectureRepository;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.repository.VacationRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    private TeacherRepository teacherRepository;
    private LectureRepository lectureRepository;
    private VacationRepository vacationRepository;
    private VacationService vacationService;
    private UniversityProperties universityProperties;

    public TeacherService(TeacherRepository jdbcTeacherRepository, LectureRepository lectureRepository, VacationRepository vacationRepository,
                          VacationService vacationService, UniversityProperties universityProperties) {
        this.teacherRepository = jdbcTeacherRepository;
        this.lectureRepository = lectureRepository;
        this.vacationRepository = vacationRepository;
        this.vacationService = vacationService;
        this.universityProperties=universityProperties;
    }

    public void create(Teacher teacher) {
        logger.debug("Creating a new teacher: {} ", teacher);
        verifyIsUnique(teacher);
        verifyHasEnoughVacationDays(teacher);
        teacherRepository.save(teacher);
    }

    public Page<Teacher> findAll(Pageable pageable) {
        logger.debug("Retrieving page {}, size {}, sort {}", pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort());

        return teacherRepository.findAll(pageable);
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findById(int id) {
        return teacherRepository.findById(id);
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
        teacherRepository.save(teacher);
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
        Teacher teacher = getById(id);
        verifyHasNoLectures(teacher);
        teacherRepository.delete(teacher);
    }

    private void verifyHasEnoughVacationDays(Teacher teacher) {
        logger.debug("Verifying that teacher has enough vacation days");
        List<Vacation> vacations = teacher.getVacations();
        if (vacations == null) {
            return;
        }
        int allowedDays = universityProperties.getVacationDays().get(teacher.getDegree());
        Map<Integer, Long> daysCountByYears = vacationService.countDaysByYears(vacations);
        long maxDays = daysCountByYears.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getValue();
        if (maxDays > allowedDays) {
            throw new VacationInsufficientDaysException(
                    String.format("Teacher has maximum %s vacation days per year, can't assign %s days", allowedDays,
                            maxDays));
        }
    }

    public void verifyIsUnique(Teacher teacher) {
        if (teacherRepository.findByFirstNameAndLastNameAndEmail(teacher.getFirstName(), teacher.getLastName(),
                teacher.getEmail()).isPresent()) {
            throw new EntityNotUniqueException(
                    String.format("Teacher %s %s with email %s already exists, can't create duplicate",
                            teacher.getFirstName(), teacher.getLastName(), teacher.getEmail()));
        }
    }

    private void verifyCanTeachScheduledLectures(Teacher teacher) {
        List<Subject> requiredSubjects = lectureRepository.findByTeacher(teacher)
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
        if (!lectureRepository.findByTeacher(teacher).isEmpty()) {
            throw new TeacherBusyException(String.format("Teacher %s %s has scheduled lecture(s), can't delete",
                    teacher.getFirstName(), teacher.getLastName()));
        }
    }


    public List<Teacher> findBySubstring(String substring) {
        return teacherRepository.findByFirstNameContainingOrLastNameContainingAllIgnoreCase(substring, substring);
    }

    public List<Teacher> getReplacementTeachers(Lecture lecture) {
        var candidates = teacherRepository.getReplacementCandidates(
                lecture.getSubject().getId(), lecture.getTeacher().getId());
        var suitableTeachers = new ArrayList<>(candidates);
        logger.debug("Found {} candidates from db", candidates.size());
        if (candidates.size() == 0) {
            return new ArrayList<>();
        }
        var date = lecture.getDate();
        var timeslot = lecture.getTimeslot();
        for (Teacher candidate : candidates) {
            if (lectureRepository.findByDateAndTimeslotAndTeacher(date, timeslot, candidate).isPresent()) {
                logger.debug("{} {} is not suitable: will be reading another lecture", candidate.getFirstName(), candidate.getLastName());
                suitableTeachers.remove(candidate);
            }
        }
        for (Teacher candidate : candidates) {
            if (vacationRepository.findByTeacher(candidate).stream()
                    .anyMatch(v -> (!date.isBefore(v.getStartDate()) && !date.isAfter(v.getEndDate())))) {
                logger.debug("{} {} is not suitable: will be on vacation", candidate.getFirstName(), candidate.getLastName());
                suitableTeachers.remove(candidate);
            }
        }
        logger.debug("{} valid candidates left", suitableTeachers.size());
        return suitableTeachers;
    }
}
