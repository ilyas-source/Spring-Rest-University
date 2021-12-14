package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.UniversityProperties;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.exception.GroupOverflowException;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.repository.StudentRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private StudentRepository studentRepository;
    private UniversityProperties universityProperties;

    public StudentService(StudentRepository studentRepository, UniversityProperties universityProperties) {
        this.studentRepository = studentRepository;
        this.universityProperties = universityProperties;
    }

    public void create(Student student) {
        logger.debug("Creating a new student: {} ", student);
        verifyStudentIsUnique(student);
        verifyGroupIsNotOverflowed(student);
        studentRepository.save(student);
    }

    public void verifyStudentIsUnique(Student student) {
        if (studentRepository.findByFirstNameAndLastNameAndBirthDate(student.getFirstName(), student.getLastName(), student.getBirthDate())
                .isPresent()) {
            throw new EntityNotUniqueException(String.format("Student %s %s, born %s already exists, can't create duplicate",
                    student.getFirstName(), student.getLastName(), student.getBirthDate()));
        }
    }

    public void verifyGroupIsNotOverflowed(Student student) {
        if (studentRepository.countByGroup(student.getGroup()) > universityProperties.getMaxStudents() - 1) {
            throw new GroupOverflowException(
                    String.format("Group limit of %s students reached, can't add more", universityProperties.getMaxStudents()));
        }
    }

    public Optional<Student> findById(int id) {
        return studentRepository.findById(id);
    }

    public Student getById(int id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find student by id " + id));
    }

    public void update(Student student) {
        logger.debug("Updating student: {} ", student);
        studentRepository.save(student);
    }

    public void delete(int id) {
        logger.debug("Deleting student by id: {} ", id);
        verifyIdExists(id);
        studentRepository.delete(getById(id));
    }

    private void verifyIdExists(int id) {
        if (studentRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(String.format("Student with id:%s not found, nothing to delete", id));
        }
    }

    public Page<Student> findAll(Pageable pageable) {
        logger.debug("Retrieving page {}, size {}, sort {}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        return studentRepository.findAll(pageable);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public List<Student> findBySubstring(String substring) {
        return studentRepository.findByFirstNameContainingOrLastNameContainingAllIgnoreCase(substring, substring);
    }
}
