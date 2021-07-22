package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLectures;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.expectedSubject1;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.expectedSubjects;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.SubjectDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.SubjectAssignedToTeacherException;
import ua.com.foxminded.university.exception.SubjectScheduledToLectureException;
import ua.com.foxminded.university.model.Subject;

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
	String expected = "Subject is assigned for 1 or more teachers, can't delete";
	when(subjectDao.findById(1)).thenReturn(Optional.of(expectedSubject1));
	when(subjectDao.countAssignments(expectedSubject1)).thenReturn(3);

	Throwable thrown = assertThrows(SubjectAssignedToTeacherException.class,
		() -> subjectService.delete(1));

	assertEquals(expected, thrown.getMessage());
	verify(subjectDao, never()).delete(1);
    }

    @Test
    void givenScheduledSubjectId_onDelete_shouldThrowException() {
	String expected = "Subject is sheduled for 1 or more lectures, can't delete";
	when(subjectDao.findById(1)).thenReturn(Optional.of(expectedSubject1));
	when(lectureDao.findBySubject(expectedSubject1)).thenReturn(expectedLectures);

	Throwable thrown = assertThrows(SubjectScheduledToLectureException.class,
		() -> subjectService.delete(1));

	assertEquals(expected, thrown.getMessage());
	verify(subjectDao, never()).delete(1);
    }

    @Test
    void givenIncorrectSubjectId_onDelete_shouldThrowException() {
	String expected = "Subject with id=1 not found, nothing to delete";

	Throwable thrown = assertThrows(EntityNotFoundException.class,
		() -> subjectService.delete(1));

	assertEquals(expected, thrown.getMessage());
	verify(subjectDao, never()).delete(1);
    }
}