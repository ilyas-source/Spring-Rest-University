package ua.com.foxminded.university.service;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

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
	var canCreate = isUnique(teacher) && hasEnoughVacationDays(teacher);
	if (canCreate) {
	    teacherDao.create(teacher);
	}
    }

    private boolean hasEnoughVacationDays(Teacher teacher) {
	int allowedDays = vacationDays.get(teacher.getDegree());
	List<Vacation> vacations = teacher.getVacations();
	Map<Integer, Long> daysCountByYears = vacationService.countDaysByYears(vacations);
	long maxDays = daysCountByYears.entrySet()
		.stream()
		.max(Comparator.comparing(Map.Entry::getValue))
		.get()
		.getValue();
	return maxDays <= allowedDays;
    }

    public boolean isUnique(Teacher teacher) {
	return teacherDao.findByNameAndEmail(teacher.getFirstName(), teacher.getLastName(), teacher.getEmail()).isEmpty();
    }

    public List<Teacher> findAll() {
	return teacherDao.findAll();
    }

    public Optional<Teacher> findById(int id) {
	return teacherDao.findById(id);
    }

    public void update(Teacher teacher) {
	logger.debug("Updating teacher: {} ", teacher);
	var canUpdate = canTeachAllScheduledLectures(teacher)
		&& hasEnoughVacationDays(teacher);
	if (canUpdate) {
	    teacherDao.update(teacher);
	}
    }

    private boolean canTeachAllScheduledLectures(Teacher teacher) {
	List<Subject> requiredSubjects = lectureDao.findByTeacher(teacher)
		.stream()
		.map(Lecture::getSubject)
		.collect(Collectors.toList());

	return teacher.getSubjects().containsAll(requiredSubjects);
    }

    private boolean hasNoLectures(Teacher teacher) {
	return lectureDao.findByTeacher(teacher).isEmpty();
    }

    public void delete(int id) {
	logger.debug("Deleting teacher by id: {} ", id);
	if (teacherDao.findById(id)
		.filter(this::hasNoLectures)
		.isPresent()) {
	    teacherDao.delete(id);
	}
    }
}
