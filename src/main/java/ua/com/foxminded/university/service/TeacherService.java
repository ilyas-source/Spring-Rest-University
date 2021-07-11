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
	} else {
	    System.out.println("Teacher with this name and email already exists, can't create");
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
	} else {
	    System.out.println("New teacher can't teach all needed subjects, can't update");
	}
    }

    private boolean canTeachAllScheduledLectures(Teacher teacher) {
	List<Subject> requiredSubjects = lectureDao.findByTeacher(teacher)
		.stream()
		.flatMap(l -> Stream.of(l.getSubject()))
		.collect(Collectors.toList());

	return teacher.getSubjects().containsAll(requiredSubjects);
    }

    private boolean noLecturesScheduled(Teacher teacher) {
	return lectureDao.findByTeacher(teacher).isEmpty();
    }

    public void delete(int id) {
	boolean canDelete = idExists(id) && noLecturesScheduled(teacherDao.findById(id).get());
	if (canDelete) {
	    teacherDao.delete(id);
	} else {
	    System.out.println("Can't delete teacher");
	}
    }

    private boolean idExists(int id) {
	return teacherDao.findById(id).isPresent();
    }
}
