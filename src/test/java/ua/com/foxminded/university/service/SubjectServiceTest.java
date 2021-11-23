package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.SubjectDao;
import ua.com.foxminded.university.exception.EntityInUseException;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.model.Subject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.service.LectureServiceTest.TestData.expectedLectures;
import static ua.com.foxminded.university.service.SubjectServiceTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectDao subjectDao;
    @Mock
    private LectureDao lectureDao;
    @InjectMocks
    private SubjectService subjectService;

    @Test
    void onFindAll_shouldReturnCorrectList() {
        when(subjectDao.findAll()).thenReturn(expectedSubjects);

        assertEquals(expectedSubjects, subjectService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectSubject() {
        when(subjectDao.findById(1)).thenReturn(Optional.of(expectedSubject1));
        Optional<Subject> expected = Optional.of(expectedSubject1);

        Optional<Subject> actual = subjectService.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenSubject_onCreate_shouldCallDaoCreate() {
        subjectService.create(expectedSubject1);

        verify(subjectDao).create(expectedSubject1);
    }

    @Test
    void givenSubject_onUpdate_shouldCallDaoUpdate() {
        subjectService.update(expectedSubject1);

        verify(subjectDao).update(expectedSubject1);
    }

    @Test
    void givenAssignedSubjectId_onDelete_shouldThrowException() {
        String expected = "Subject Test Economics is assigned to teacher(s), can't delete";
        when(subjectDao.findById(1)).thenReturn(Optional.of(expectedSubject1));
        when(subjectDao.countAssignments(expectedSubject1)).thenReturn(3L);

        Throwable thrown = assertThrows(EntityInUseException.class,
                () -> subjectService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(subjectDao, never()).delete(any());
    }

    @Test
    void givenScheduledSubjectId_onDelete_shouldThrowException() {
        String expected = "Subject Test Economics is scheduled for lecture(s), can't delete";
        when(subjectDao.findById(1)).thenReturn(Optional.of(expectedSubject1));
        when(lectureDao.findBySubject(expectedSubject1)).thenReturn(expectedLectures);

        Throwable thrown = assertThrows(EntityInUseException.class,
                () -> subjectService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(subjectDao, never()).delete(expectedSubject1);
    }

    @Test
    void givenIncorrectSubjectId_onDelete_shouldThrowException() {
        String expected = "Can't find subject by id 1";
        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> subjectService.delete(1));
        assertEquals(expected, thrown.getMessage());
        verify(subjectDao, never()).delete(any());
    }

    @Test
    void givenFreeSubject_onDelete_shouldCallDaoDelete() {
        when(subjectDao.findById(1)).thenReturn(Optional.of(expectedSubject1));
        when(subjectDao.countAssignments(expectedSubject1)).thenReturn(0L);
        when(lectureDao.findBySubject(expectedSubject1)).thenReturn(new ArrayList<>());

        subjectService.delete(1);

        verify(subjectDao).delete(expectedSubject1);
    }

    @Test
    void givenSubjectWithOldNameAndNewId_onUpdate_shouldThrowException() {
        String expected = "Subject Test Philosophy already exists";
        Subject subject = new Subject(5, "Test Philosophy", "Test");
        when(subjectDao.findByName("Test Philosophy")).thenReturn(Optional.of(expectedSubject2));

        Throwable thrown = assertThrows(EntityNotUniqueException.class,
                () -> subjectService.update(subject));

        assertEquals(expected, thrown.getMessage());
        verify(subjectDao, never()).update(subject);
    }

    public interface TestData {
        Subject subjectToCreate = new Subject(5, "test", "test");
        Subject subjectToUpdate = new Subject(2, "test", "test");

        Subject expectedSubject1 = new Subject(1, "Test Economics", "Base economics");
        Subject expectedSubject2 = new Subject(2, "Test Philosophy", "Base philosophy");
        Subject expectedSubject3 = new Subject(3, "Test Chemistry", "Base chemistry");
        Subject expectedSubject4 = new Subject(4, "Test Radiology", "Explore radiation");

        List<Subject> expectedSubjects = new ArrayList<>(
                Arrays.asList(expectedSubject1, expectedSubject2, expectedSubject3, expectedSubject4));
    }
}