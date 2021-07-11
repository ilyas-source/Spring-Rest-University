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

    public void create(Student student) {
	if (isUnique(student)) {
	    studentDao.create(student);
	} else {
	    System.out.println("Student with this name and birthdate already exists, can't create");
	}
    }

    public boolean isUnique(Student student) {
	return studentDao.findByNameAndBirthDate(student.getFirstName(), student.getLastName(), student.getBirthDate()).isEmpty();
    }

    public List<Student> findAll() {
	return studentDao.findAll();
    }

    public Optional<Student> findById(int choice) {
	return studentDao.findById(choice);
    }

    public void update(Student student) {
	studentDao.update(student);
    }

    public void delete(int id) {
	boolean canDelete = idExists(id);
	if (canDelete) {
	    studentDao.delete(id);
	} else {
	    System.out.println("Can't delete student");
	}
    }

    private boolean idExists(int id) {
	return studentDao.findById(id).isPresent();
    }
}
