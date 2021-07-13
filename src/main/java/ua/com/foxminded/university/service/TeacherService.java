package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;

@Service
public class TeacherService {

    private TeacherDao teacherDao;
    private LectureDao lectureDao;

    public TeacherService(TeacherDao jdbcTeacherDao, LectureDao lectureDao) {
	this.teacherDao = jdbcTeacherDao;
	this.lectureDao = lectureDao;
    }

    public void create(Teacher teacher) {
	if (isUnique(teacher)) {
	    teacherDao.create(teacher);
	}
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
	if (canTeachAllScheduledLectures(teacher)) {
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
