package ua.com.foxminded.university.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.SubjectDao;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectDao subjectDao;
    @Mock
    private LectureDao lectureDao;
    @InjectMocks
    private SubjectService subjectService;

    @Test
    void onFindAll_shouldCallDaoFindAll() {
	subjectService.findAll();

	verify(subjectDao).findAll();
    }

    @Test
    void givenId_onFindById_shouldCallDaoFindById() {
	subjectService.findById(1);

	verify(subjectDao).findById(1);
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
    void givenAssignedSubjectId_onDelete_shouldNotCallDaoDelete() {
	when(subjectDao.findById(1)).thenReturn(Optional.of(expectedSubject1));
	when(subjectDao.countAssignments(expectedSubject1)).thenReturn(3);

	subjectService.delete(1);

	verify(subjectDao, never()).delete(1);
    }

    @Test
    void givenScheduledSubjectId_onDelete_shouldNotCallDaoDelete() {
	when(subjectDao.findById(1)).thenReturn(Optional.of(expectedSubject1));
	when(lectureDao.findBySubject(expectedSubject1)).thenReturn(expectedLectures);

	subjectService.delete(1);

	verify(subjectDao, never()).delete(1);
    }

    @Test
    void givenIncorrectSubjectId_onDelete_shouldNotCallDaoDelete() {
	subjectService.delete(1);

	verify(subjectDao, never()).delete(1);
    }
}