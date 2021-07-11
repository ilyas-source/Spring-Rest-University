package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Teacher;

@Service
public class TeacherService {

    private TeacherDao teacherDao;

    public TeacherService(TeacherDao jdbcTeacherDao) {
	this.teacherDao = jdbcTeacherDao;
    }

    public void create(Teacher teacher) {
	// проверить уникальность препода
	teacherDao.create(teacher);
    }

    public List<Teacher> findAll() {
	return teacherDao.findAll();
    }

    public Optional<Teacher> findById(int choice) {
	return teacherDao.findById(choice);
    }

    public void update(Teacher newTeacher) {
	// проверить уникальность препода
	// проверить, что с новыми предметами препод сможет вести свои лекции
	teacherDao.update(newTeacher);
    }

    public void delete(int id) {
	boolean canDelete = idExists(id);
	// // проверить, что он не ведет лекций
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
