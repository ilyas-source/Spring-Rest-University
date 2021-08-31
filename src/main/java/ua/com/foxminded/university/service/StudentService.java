package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.exception.GroupOverflowException;
import ua.com.foxminded.university.model.Student;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        verifyStudentIsUnique(student);
        verifyGroupIsNotOverflowed(student);
        studentDao.create(student);
    }

    public void verifyStudentIsUnique(Student student) {
        if (studentDao.findByNameAndBirthDate(student.getFirstName(), student.getLastName(), student.getBirthDate())
                .isPresent()) {
            throw new EntityNotUniqueException(String.format("Student %s %s, born %s already exists, can't create duplicate",
                    student.getFirstName(), student.getLastName(), student.getBirthDate()));
        }
    }

    public void verifyGroupIsNotOverflowed(Student student) {
        if (studentDao.countInGroup(student.getGroup()) > maxStudentsInGroup - 1) {
            throw new GroupOverflowException(
                    String.format("Group limit of %s students reached, can't add more", maxStudentsInGroup));
        }
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
        verifyIdExists(id);
        studentDao.delete(id);
    }

    private void verifyIdExists(int id) {
        if (!studentDao.findById(id).isPresent()) {
            throw new EntityNotFoundException(String.format("Student with id:%s not found, nothing to delete", id));
        }
    }

    public Page<Student> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Student> students = findAll();
        List<Student> list;

        if (students.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, students.size());
            list = students.subList(startItem, toIndex);
        }

        return new PageImpl<Student>(list, PageRequest.of(currentPage, pageSize), students.size());
    }
}
