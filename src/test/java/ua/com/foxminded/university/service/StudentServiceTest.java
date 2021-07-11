package ua.com.foxminded.university.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.StudentDaoTest.TestData.expectedStudent1;
import static ua.com.foxminded.university.dao.StudentDaoTest.TestData.expectedStudent2;

import java.util.Optional;

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
    void givenUniqueStudent_onCreate_shouldCallDaoCreate() {
	studentService.create(expectedStudent1);

	verify(studentDao).create(expectedStudent1);
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