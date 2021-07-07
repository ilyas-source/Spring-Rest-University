package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.StudentDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.jdbc.JdbcStudentDao;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private JdbcStudentDao studentDao;
    @InjectMocks
    private StudentService studentService;

    @Test
    void onFindAll_shouldReturnAllStudents() {
	when(studentDao.findAll()).thenReturn(expectedStudents);

	assertEquals(expectedStudents, studentService.findAll());
    }

    @Test
    void givenCorrectId_onFindById_shouldReturnOptionalWithCorrectStudent() {
	when(studentDao.findById(1)).thenReturn(Optional.of(expectedStudent1));

	assertEquals(Optional.of(expectedStudent1), studentService.findById(1));
    }

    @Test
    void givenStudent_onCreate_shouldCallCreate() {
	studentService.create(expectedStudent1);

	verify(studentDao).create(expectedStudent1);
    }

    @Test
    void givenStudent_onUpdate_shouldCallUpdate() {
	studentService.update(expectedStudent1);

	verify(studentDao).update(expectedStudent1);
    }

    @Test
    void givenStudent_onDelete_shouldCallDelete() {
	studentService.delete(1);

	verify(studentDao).delete(1);
    }
}