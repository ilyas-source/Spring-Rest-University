package ua.com.foxminded.university.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.expectedTeacher1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.TeacherDao;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherDao teacherDao;
    @InjectMocks
    private TeacherService teacherService;

    @Test
    void onFindAll_shouldCallDaoFindAll() {
	teacherService.findAll();

	verify(teacherDao).findAll();
    }

    @Test
    void givenId_onFindById_shouldCallDaoFindById() {
	teacherService.findById(1);

	verify(teacherDao).findById(1);
    }

    @Test
    void givenTeacher_onCreate_shouldCallDaoCreate() {
	teacherService.create(expectedTeacher1);

	verify(teacherDao).create(expectedTeacher1);
    }

    @Test
    void givenTeacher_onUpdate_shouldCallDaoUpdate() {
	teacherService.update(expectedTeacher1);

	verify(teacherDao).update(expectedTeacher1);
    }

    @Test
    void givenIncorrectTeacherId_onDelete_shouldNotCallDaoDelete() {
	teacherService.delete(1);

	verify(teacherDao, never()).delete(1);
    }
}