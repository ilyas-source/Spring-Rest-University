package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;

@PropertySource("classpath:university.properties")
@Service
public class TeacherService {

    private TeacherDao teacherDao;
    private LectureDao lectureDao;
    private VacationService vacationService;

    @Value("${bachelor.vacationdays}")
    public int bachelorVacationDays;

    @Value("${doctor.vacationdays}")
    public int doctorVacationDays;

    @Value("${master.vacationdays}")
    public int masterVacationDays;

    public TeacherService(TeacherDao jdbcTeacherDao, LectureDao lectureDao, VacationService vacationService) {
	this.teacherDao = jdbcTeacherDao;
	this.lectureDao = lectureDao;
	this.vacationService = vacationService;
    }

    public void create(Teacher teacher) {
	boolean canCreate = isUnique(teacher) && hasEnoughVacationDays(teacher);
	if (canCreate) {
	    teacherDao.create(teacher);
	}
    }

    private boolean hasEnoughVacationDays(Teacher teacher) {
	int allowedDays = 0;
	switch (teacher.getDegree()) {
	case BACHELOR:
	    allowedDays = bachelorVacationDays;
	    break;
	case DOCTOR:
	    allowedDays = doctorVacationDays;
	    break;
	case MASTER:
	    allowedDays = masterVacationDays;
	    break;
	default:
	    break;
	}
	int totalVacations = teacher.getVacations()
		.stream()
		.flatMap(v -> Stream.of(vacationService.countLength(v)))
		.reduce(0, Integer::sum);
	return totalVacations <= allowedDays;
    }

    public boolean isUnique(Teacher teacher) {
	return teacherDao.findByNameAndEmail(teacher.getFirstName(), teacher.getLastName(), teacher.getEmail()).isEmpty();
    }

    public List<Teacher> findAll() {
	return teacherDao.findAll();
    }

    public Optional<Teacher> findById(int choice) {
	return teacherDao.findById(choice);
    }

    public void update(Teacher teacher) {
	boolean canUpdate = canTeachAllScheduledLectures(teacher)
		&& hasEnoughVacationDays(teacher);
	if (canUpdate) {
	    teacherDao.update(teacher);
	}
    }

    private boolean canTeachAllScheduledLectures(Teacher teacher) {
	List<Subject> requiredSubjects = lectureDao.findByTeacher(teacher)
		.stream()
		.flatMap(l -> Stream.of(l.getSubject()))
		.collect(Collectors.toList());

	return teacher.getSubjects().containsAll(requiredSubjects);
    }

    private boolean hasNoLectures(Teacher teacher) {
	return lectureDao.findByTeacher(teacher).isEmpty();
    }

    public void delete(int id) {
	Optional<Teacher> optionalTeacher = teacherDao.findById(id);
	boolean canDelete = optionalTeacher.isPresent() && hasNoLectures(optionalTeacher.get());
	if (canDelete) {
	    teacherDao.delete(id);
	}
    }
}
