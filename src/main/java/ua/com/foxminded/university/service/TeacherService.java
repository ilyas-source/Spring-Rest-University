package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;

@PropertySource("classpath:university.properties")
@Service
public class TeacherService {

    private TeacherDao teacherDao;
    private LectureDao lectureDao;
    private VacationService vacationService;

    @Value("#{${vacationdays.map}}")
    public Map<String, Integer> vacationDays;

    public TeacherService(TeacherDao jdbcTeacherDao, LectureDao lectureDao, VacationService vacationService) {
	this.teacherDao = jdbcTeacherDao;
	this.lectureDao = lectureDao;
	this.vacationService = vacationService;
    }

    public void create(Teacher teacher) {
	var canCreate = isUnique(teacher) && hasEnoughVacationDays(teacher);
	if (canCreate) {
	    teacherDao.create(teacher);
	}
    }

    private boolean hasEnoughVacationDays(Teacher teacher) {
	int allowedDays = vacationDays.get(teacher.getDegree().toString());

	long totalVacations = teacher.getVacations()
		.stream()
		.mapToLong(vacationService::getDaysDuration)
		.sum();

	return totalVacations <= allowedDays;
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
	if (teacherDao.findById(id)
		.filter(this::hasNoLectures)
		.isPresent()) {
	    teacherDao.delete(id);
	}
    }
}
