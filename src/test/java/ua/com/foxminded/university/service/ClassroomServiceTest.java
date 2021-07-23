package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.duplicateNameClassroom;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.expectedClassrooms;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.invalidCapacityClassroom;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLecture1;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLecture2;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLectures;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.exception.ClassroomInvalidCapacityException;
import ua.com.foxminded.university.exception.ClassroomOccupiedException;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.model.Classroom;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceTest {

    @Mock
    private ClassroomDao classroomDao;
    @Mock
    private LectureDao lectureDao;
    @Mock
    private GroupDao groupDao;
    @Mock
    private LectureService lectureService;
    @InjectMocks
    private ClassroomService classroomService;

    @Test
    void onFindAll_shouldReturnCorrectList() {
	when(classroomDao.findAll()).thenReturn(expectedClassrooms);

	assertEquals(expectedClassrooms, classroomService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectClassroom() {
	when(classroomDao.findById(1)).thenReturn(Optional.of(expectedClassroom1));
	Optional<Classroom> expected = Optional.of(expectedClassroom1);

	Optional<Classroom> actual = classroomService.findById(1);

	assertEquals(expected, actual);
    }

    @Test
    void givenGoodClassroom_onCreate_shouldCallDaoCreate() {
	when(classroomDao.findByName(expectedClassroom1.getName())).thenReturn(Optional.of(expectedClassroom1));

	classroomService.create(expectedClassroom1);

	verify(classroomDao).create(expectedClassroom1);
    }

    @Test
    void givenClassroomWithSameNameAndId_onUpdate_shouldCallDaoUpdate() {
	when(classroomDao.findByName(expectedClassroom1.getName())).thenReturn(Optional.of(expectedClassroom1));

	classroomService.update(expectedClassroom1);

	verify(classroomDao).update(expectedClassroom1);
    }

    @Test
    void givenExistingClassroom_onDelete_shouldCallDaoDelete() {
	when(classroomDao.findById(1)).thenReturn(Optional.of(expectedClassroom1));

	classroomService.delete(1);

	verify(classroomDao).delete(1);
    }

    @Test
    void givenSmallClassroom_onUpdate_shouldThrowException() {
	String expected = "Classroom too small: required 501, but was 500";
	when(lectureDao.findByClassroom(expectedClassroom1)).thenReturn(expectedLectures);
	when(lectureService.countStudentsInLecture(expectedLecture1)).thenReturn(501);
	when(lectureService.countStudentsInLecture(expectedLecture2)).thenReturn(200);

	Throwable thrown = assertThrows(ClassroomInvalidCapacityException.class,
		() -> classroomService.update(expectedClassroom1));

	assertEquals(expected, thrown.getMessage());
	verify(classroomDao, never()).update(expectedClassroom1);
    }

    @Test
    void givenOccupiedClassroom_onDelete_shouldThrowException() {
	String expected = "There are scheduled lectures, can't delete classroom Big physics auditory";
	when(classroomDao.findById(1)).thenReturn(Optional.of(expectedClassroom1));
	when(lectureDao.findByClassroom(expectedClassroom1)).thenReturn(expectedLectures);

	Throwable thrown = assertThrows(ClassroomOccupiedException.class, () -> classroomService.delete(1));

	assertEquals(expected, thrown.getMessage());
	verify(classroomDao, never()).delete(1);
    }

    @Test
    void givenClassroomWithExistingName_onCreate_shouldThrowException() {
	String expected = "Classroom Big physics auditory already exists";
	when(classroomDao.findByName(duplicateNameClassroom.getName())).thenReturn(Optional.of(expectedClassroom1));

	Throwable thrown = assertThrows(EntityNotUniqueException.class,
		() -> classroomService.create(duplicateNameClassroom));

	assertEquals(expected, thrown.getMessage());
	verify(classroomDao, never()).create(duplicateNameClassroom);
    }

    @Test
    void givenClassroomWithInvalidCapacity_onCreate_shouldThrowException() {
	String expected = "Classroom capacity should not be less than 1";

	Throwable thrown = assertThrows(ClassroomInvalidCapacityException.class,
		() -> classroomService.create(invalidCapacityClassroom));

	assertEquals(expected, thrown.getMessage());
	verify(classroomDao, never()).create(invalidCapacityClassroom);
    }

    @Test
    void givenIncorrectClassroomId_onDelete_shouldThrowException() {
	String expected = "Classroom id:1 not found, nothing to delete";

	Throwable thrown = assertThrows(EntityNotFoundException.class,
		() -> classroomService.delete(1));

	assertEquals(expected, thrown.getMessage());
	verify(classroomDao, never()).delete(1);
    }
}