package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.StudentDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Student;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    private static final int MAX_STUDENTS_IN_GROUP = 30;

    @BeforeEach
    void init() {
	ReflectionTestUtils.setField(studentService, "maxStudentsInGroup", MAX_STUDENTS_IN_GROUP);
    }

    @Mock
    private StudentDao studentDao;
    @InjectMocks
    private StudentService studentService;

    @Test
    void onFindAll_shouldReturnCorrectList() {
	when(studentDao.findAll()).thenReturn(expectedStudents);

	assertEquals(expectedStudents, studentService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectStudent() {
	when(studentDao.findById(1)).thenReturn(Optional.of(expectedStudent1));
	Optional<Student> expected = Optional.of(expectedStudent1);

	Optional<Student> actual = studentService.findById(1);

	assertEquals(expected, actual);
    }

    @Test
    void givenUniqueStudent_onCreate_shouldCallDaoCreate() {
	studentService.create(expectedStudent1);

	verify(studentDao).create(expectedStudent1);
    }

    @Test
    void givenExcessiveStudent_onCreate_shouldNotCallDaoCreate() {
	when(studentDao.countInGroup(expectedStudent1.getGroup())).thenReturn(30);
	studentService.create(expectedStudent1);

	verify(studentDao, never()).create(expectedStudent1);
    }

    @Test
    void givenNonUniqueStudent_onCreate_shouldNotCallDaoCreate() {
	when(studentDao.findByNameAndBirthDate(expectedStudent1.getFirstName(), expectedStudent1.getLastName(),
		expectedStudent1.getBirthDate())).thenReturn(Optional.of(expectedStudent2));
	studentService.create(expectedStudent1);

	verify(studentDao, never()).create(expectedStudent1);
    }

    @Test
    void givenStudent_onUpdate_shouldCallDaoUpdate() {
	studentService.update(expectedStudent1);

	verify(studentDao).update(expectedStudent1);
    }

    @Test
    void givenExistingStudentId_onDelete_shouldCallDaoDelete() {
	when(studentDao.findById(1)).thenReturn(Optional.of(expectedStudent1));
	studentService.delete(1);

	verify(studentDao).delete(1);
    }

    @Test
    void givenIncorrectStudentId_onDelete_shouldNotCallDaoDelete() {
	studentService.delete(1);

	verify(studentDao, never()).delete(1);
    }
}