package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import ua.com.foxminded.university.model.Teacher;

@Service
public class TeacherService {

    private TeacherDao jdbcTeacherDao;

    public TeacherService(TeacherDao jdbcTeacherDao) {
	this.jdbcTeacherDao = jdbcTeacherDao;
    }

    public void create(Teacher createTeacher) {
	jdbcTeacherDao.create(createTeacher);
    }

    public List<Teacher> findAll() {
	return jdbcTeacherDao.findAll();
    }

    public Optional<Teacher> findById(int choice) {
	return jdbcTeacherDao.findById(choice);
    }

    public void update(Teacher newTeacher) {
	jdbcTeacherDao.update(newTeacher);
    }

    public void delete(int id) {
	jdbcTeacherDao.delete(id);
    }
}
