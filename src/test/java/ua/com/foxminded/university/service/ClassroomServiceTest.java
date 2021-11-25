package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.exception.ClassroomInvalidCapacityException;
import ua.com.foxminded.university.exception.ClassroomOccupiedException;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.repository.ClassroomRepository;
import ua.com.foxminded.university.repository.LectureRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.service.ClassroomServiceTest.TestData.*;
import static ua.com.foxminded.university.service.LectureServiceTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceTest {

    @Mock
    private ClassroomRepository classroomRepository;
    @Mock
    private LectureRepository lectureRepository;
    @Mock
    private LectureService lectureService;
    @InjectMocks
    private ClassroomService classroomService;

    @Test
    void onFindAll_shouldReturnCorrectList() {
        when(classroomRepository.findAll()).thenReturn(expectedClassrooms);

        assertEquals(expectedClassrooms, classroomService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectClassroom() {
        when(classroomRepository.findById(1)).thenReturn(Optional.of(expectedClassroom1));
        Optional<Classroom> expected = Optional.of(expectedClassroom1);

        Optional<Classroom> actual = classroomService.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenGoodClassroom_onCreate_shouldCallRepositoryCreate() {
        when(classroomRepository.findByName(expectedClassroom1.getName())).thenReturn(Optional.of(expectedClassroom1));

        classroomService.create(expectedClassroom1);

        verify(classroomRepository).save(expectedClassroom1);
    }

    @Test
    void givenClassroomWithSameNameAndId_onUpdate_shouldCallRepositoryUpdate() {
        when(classroomRepository.findByName(expectedClassroom1.getName())).thenReturn(Optional.of(expectedClassroom1));

        classroomService.update(expectedClassroom1);

        verify(classroomRepository).save(expectedClassroom1);
    }

    @Test
    void givenExistingClassroom_onDelete_shouldCallRepositoryDelete() {
        when(classroomRepository.findById(1)).thenReturn(Optional.of(expectedClassroom1));

        classroomService.delete(1);

        verify(classroomRepository).delete(expectedClassroom1);
    }

    @Test
    void givenSmallClassroom_onUpdate_shouldThrowException() {
        String expected = "Classroom too small: required 501, but was 500";
        when(lectureRepository.findByClassroom(expectedClassroom1)).thenReturn(expectedLectures);
        when(lectureService.countStudentsInLecture(expectedLecture1)).thenReturn(501);
        when(lectureService.countStudentsInLecture(expectedLecture2)).thenReturn(200);

        Throwable thrown = assertThrows(ClassroomInvalidCapacityException.class,
                () -> classroomService.update(expectedClassroom1));

        assertEquals(expected, thrown.getMessage());
        verify(classroomRepository, never()).save(expectedClassroom1);
    }

    @Test
    void givenOccupiedClassroom_onDelete_shouldThrowException() {
        String expected = "There are scheduled lectures, can't delete classroom Big physics auditory";
        when(classroomRepository.findById(1)).thenReturn(Optional.of(expectedClassroom1));
        when(lectureRepository.findByClassroom(expectedClassroom1)).thenReturn(expectedLectures);

        Throwable thrown = assertThrows(ClassroomOccupiedException.class, () -> classroomService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(classroomRepository, never()).delete(any());
    }

    @Test
    void givenClassroomWithExistingName_onCreate_shouldThrowException() {
        String expected = "Classroom Big physics auditory already exists";
        when(classroomRepository.findByName(duplicateNameClassroom.getName())).thenReturn(Optional.of(expectedClassroom1));

        Throwable thrown = assertThrows(EntityNotUniqueException.class,
                () -> classroomService.create(duplicateNameClassroom));

        assertEquals(expected, thrown.getMessage());
        verify(classroomRepository, never()).save(duplicateNameClassroom);
    }

    @Test
    void givenClassroomWithInvalidCapacity_onCreate_shouldThrowException() {
        String expected = "Classroom capacity should not be less than 1";

        Throwable thrown = assertThrows(ClassroomInvalidCapacityException.class,
                () -> classroomService.create(invalidCapacityClassroom));

        assertEquals(expected, thrown.getMessage());
        verify(classroomRepository, never()).save(invalidCapacityClassroom);
    }

    @Test
    void givenIncorrectClassroomId_onDelete_shouldThrowException() {
        String expected = "Can't find classroom by id 1";

        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> classroomService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(classroomRepository, never()).delete(any());
    }

    interface TestData {
        Location location1 = new Location(1, "Phys building", 2, 22);
        Classroom expectedClassroom1 = new Classroom(1, location1, "Big physics auditory", 500);

        Location location2 = new Location(2, "Chem building", 1, 12);
        Classroom expectedClassroom2 = new Classroom(2, location2, "Small chemistry auditory", 30);

        Location location3 = new Location(3, "Chem building", 2, 12);
        Classroom expectedClassroom3 = new Classroom(3, location3, "Chemistry laboratory", 15);

        List<Classroom> expectedClassrooms = new ArrayList<>(
                Arrays.asList(expectedClassroom1, expectedClassroom2, expectedClassroom3));

        Classroom duplicateNameClassroom = new Classroom(2, location1, "Big physics auditory", 500);
        Classroom invalidCapacityClassroom = new Classroom(1, location1, "Big physics auditory", -5);
    }
}