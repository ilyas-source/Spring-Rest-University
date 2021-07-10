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

import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
import ua.com.foxminded.university.model.Classroom;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceTest {

    @Mock
    private JdbcClassroomDao classroomDao;
    @Mock
    private LectureDao lectureDao;
    @Mock
    private GroupDao groupDao;
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
    void givenGoodClassroom_onUpdate_shouldCallUpdate() throws Exception {
	classroomService.update(expectedClassroom1);

	verify(classroomDao).update(expectedClassroom1);
    }

    @Test
    void givenSmallClassroom_onUpdate_shouldThrowException() {
	Classroom smallClassroom = expectedClassroom1;
	when(lectureDao.findByClassroom(expectedClassroom1)).thenReturn(expectedLectures);
	when(lectureDao.countStudentsInLecture(expectedLecture1)).thenReturn(1000);
	when(lectureDao.countStudentsInLecture(expectedLecture2)).thenReturn(200);
	String expected = "Required minimum capacity 1000, but was 500";

	Throwable thrown = assertThrows(Exception.class, () -> {
	    classroomService.update(smallClassroom);
	});

	assertEquals(expected, thrown.getMessage());
    }

    @Test
    void givenExistingClassroom_onDelete_shouldCallDelete() throws Exception {
	when(classroomDao.findById(1)).thenReturn(Optional.of(expectedClassroom1));
	classroomService.delete(1);

	verify(classroomDao).delete(1);
    }

    @Test
    void givenNonExistingClassroom_onDelete_shouldThrowException() throws Exception {
	String expected = "Classroom with id 1 does not exist, nothing to delete";

	Throwable thrown = assertThrows(Exception.class, () -> {
	    classroomService.delete(1);
	});

	assertEquals(expected, thrown.getMessage());
    }

    @Test
    void givenOccupiedClassroom_onDelete_shouldThrowException() throws Exception {
	String expected = "Classroom has scheduled lecture(s), can't delete";
	when(classroomDao.findById(1)).thenReturn(Optional.of(expectedClassroom1));
	when(lectureDao.findByClassroom(expectedClassroom1)).thenReturn(expectedLectures);

	Throwable thrown = assertThrows(Exception.class, () -> {
	    classroomService.delete(expectedClassroom1.getId());
	});

	assertEquals(expected, thrown.getMessage());
    }

    @Test
    void givenClassroomWithExistingName_onCreate_shouldThrowException() {
	when(classroomDao.findByName(expectedClassroom1.getName())).thenReturn(Optional.of(expectedClassroom1));
	String expected = String.format("Classroom with name %s already exists, can't create", expectedClassroom1.getName());

	Throwable thrown = assertThrows(Exception.class, () -> {
	    classroomService.create(expectedClassroom1);
	});

	assertEquals(expected, thrown.getMessage());
    }

    @Test
    void givenClassroomWithInvalidCapacity_onCreate_shouldThrowException() {
	String expected = "Wrong classroom capacity, can't create";
	int capacityBackup = expectedClassroom1.getCapacity();
	expectedClassroom1.setCapacity(-5);

	Throwable thrown = assertThrows(Exception.class, () -> {
	    classroomService.create(expectedClassroom1);
	});

	expectedClassroom1.setCapacity(capacityBackup);
	assertEquals(expected, thrown.getMessage());
    }
}