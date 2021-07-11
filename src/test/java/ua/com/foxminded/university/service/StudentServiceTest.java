package ua.com.foxminded.university.service;

import static org.mockito.Mockito.verify;
import static ua.com.foxminded.university.dao.StudentDaoTest.TestData.expectedStudent1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.StudentDao;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDao studentDao;
    @InjectMocks
    private StudentService studentService;

    @Test
    void onFindAll_shouldCallDaoFindAll() {
	studentService.findAll();

	verify(studentDao).findAll();
    }

    @Test
    void givenId_onFindById_shouldCallDaoFindById() {
	studentService.findById(1);

	verify(studentDao).findById(1);
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