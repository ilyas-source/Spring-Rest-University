package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.jdbc.JdbcStudentDao;
import ua.com.foxminded.university.model.Student;

@Service
public class StudentService {

    private JdbcStudentDao jdbcStudentDao;

    public StudentService(JdbcStudentDao jdbcStudentDao) {
	this.jdbcStudentDao = jdbcStudentDao;
    }

    public void create(Student createStudent) {
	jdbcStudentDao.create(createStudent);
    }

    public List<Student> findAll() {
	return jdbcStudentDao.findAll();
    }

    public Optional<Student> findById(int choice) {
	return jdbcStudentDao.findById(choice);
    }

    public void update(Student newStudent) {
	jdbcStudentDao.update(newStudent);
    }

    public void delete(int id) {
	jdbcStudentDao.delete(id);
    }
}
