package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceTest {

    @Mock
    private JdbcClassroomDao classroomDao;
    @InjectMocks
    private ClassroomService classroomService;

    @Test
    void onFindAll_shouldReturnAllClassrooms() {
	when(classroomDao.findAll()).thenReturn(expectedClassrooms);

	assertEquals(expectedClassrooms, classroomService.findAll());
    }

    @Test
    void givenCorrectId_onFindById_shouldReturnOptionalWithCorrectClassroom() {
	when(classroomDao.findById(1)).thenReturn(Optional.of(expectedClassroom1));

	assertEquals(Optional.of(expectedClassroom1), classroomService.findById(1));
    }

    @Test
    void givenClassroom_onCreate_shouldCallCreate() throws Exception {
	classroomService.create(expectedClassroom1);

	verify(classroomDao).create(expectedClassroom1);
    }

    @Test
    void givenClassroom_onUpdate_shouldCallUpdate() throws Exception {
	classroomService.update(expectedClassroom1);

	verify(classroomDao).update(expectedClassroom1);
    }

    @Test
    void givenClassroom_onDelete_shouldCallDelete() {
	classroomService.delete(1);

	verify(classroomDao).delete(1);
    }
}