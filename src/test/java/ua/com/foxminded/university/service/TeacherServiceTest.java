package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDao;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private JdbcTeacherDao teacherDao;
    @InjectMocks
    private TeacherService teacherService;

    @Test
    void onFindAll_shouldReturnAllTeachers() {
	when(teacherDao.findAll()).thenReturn(expectedTeachers);

	assertEquals(expectedTeachers, teacherService.findAll());
    }

    @Test
    void givenCorrectId_onFindById_shouldReturnOptionalWithCorrectTeacher() {
	when(teacherDao.findById(1)).thenReturn(Optional.of(expectedTeacher1));

	assertEquals(Optional.of(expectedTeacher1), teacherService.findById(1));
    }

    @Test
    void givenTeacher_onCreate_shouldCallCreate() {
	teacherService.create(expectedTeacher1);

	verify(teacherDao).create(expectedTeacher1);
    }

    @Test
    void givenTeacher_onUpdate_shouldCallUpdate() {
	teacherService.update(expectedTeacher1);

	verify(teacherDao).update(expectedTeacher1);
    }

    @Test
    void givenTeacher_onDelete_shouldCallDelete() {
	teacherService.delete(1);

	verify(teacherDao).delete(1);
    }
}