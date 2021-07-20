package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Student;

@PropertySource("classpath:university.properties")
@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private StudentDao studentDao;

    @Value("${group.maxstudents}")
    public int maxStudentsInGroup;

    public StudentService(StudentDao studentDao) {
	this.studentDao = studentDao;
    }

    public void create(Student student) {
	logger.debug("Creating a new student: {} ", student);
	var canCreate = isUnique(student) && isNotOverpopulatingGroup(student);
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
	logger.debug("Updating student: {} ", student);
	studentDao.update(student);
    }

    public void delete(int id) {
	logger.debug("Deleting student by id: {} ", id);
	if (idExists(id)) {
	    studentDao.delete(id);
	}
    }

    private boolean idExists(int id) {
	return studentDao.findById(id).isPresent();
    }
}
