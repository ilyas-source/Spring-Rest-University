package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.GroupDaoTest.TestData.expectedGroup1;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
import ua.com.foxminded.university.model.Classroom;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceTest {

    @Mock
    private JdbcClassroomDao classroomDao;
    @Mock
    private LectureDao lectureDao;
    @InjectMocks
    private ClassroomService classroomService;

    @Test
    void givenSmallClassroom_onVerifyCapacityIsEnough_shouldThrowException() {
	Classroom smallClassroom = expectedClassroom1;
	when(lectureDao.findByClassroom(expectedClassroom1)).thenReturn(expectedLectures);
	when(lectureDao.countStudentsInLecture(expectedLecture1)).thenReturn(1000);
	when(lectureDao.countStudentsInLecture(expectedLecture2)).thenReturn(200);
	String expected = "Required minumum capacity 1000, but was 500";

	Throwable thrown = assertThrows(Exception.class, () -> {
	    classroomService.verifyCapacityIsEnough(smallClassroom);
	});

	assertEquals(expected, thrown.getMessage());
    }

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