package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Student;

@PropertySource("classpath:university.properties")
@Service
public class StudentService {

    private StudentDao studentDao;

    @Value("${group.maxstudents}")
    public int maxStudentsInGroup;

    public StudentService(StudentDao studentDao) {
	this.studentDao = studentDao;
    }

    public void create(Student student) {
	boolean canCreate = isUnique(student) && isNotOverpopulatingGroup(student);
	if (canCreate) {
	    studentDao.create(student);
	}
    }

    public boolean isUnique(Student student) {
	return studentDao.findByNameAndBirthDate(student.getFirstName(), student.getLastName(), student.getBirthDate()).isEmpty();
    }

    public boolean isNotOverpopulatingGroup(Student student) {
	return studentDao.countInGroup(student.getGroup()) <= maxStudentsInGroup - 1;
    }

    public List<Student> findAll() {
	return studentDao.findAll();
    }

    public Optional<Student> findById(int id) {
	return studentDao.findById(id);
    }

    public void update(Student student) {
	studentDao.update(student);
    }

    public void delete(int id) {
	boolean canDelete = idExists(id);
	if (canDelete) {
	    studentDao.delete(id);
	}
    }

    private boolean idExists(int id) {
	return studentDao.findById(id).isPresent();
    }
}
