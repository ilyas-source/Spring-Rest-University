package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Student;

@Service
public class StudentService {

    private StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
	this.studentDao = studentDao;
    }

    public void create(Student createStudent) {
	studentDao.create(createStudent);
    }

    public List<Student> findAll() {
	return studentDao.findAll();
    }

    public Optional<Student> findById(int choice) {
	return studentDao.findById(choice);
    }

    public void update(Student newStudent) {
	studentDao.update(newStudent);
    }

    public void delete(int id) {
	studentDao.delete(id);
    }
}
