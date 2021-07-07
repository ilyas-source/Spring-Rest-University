package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDao;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private JdbcSubjectDao subjectDao;
    @InjectMocks
    private SubjectService subjectService;

    @Test
    void onFindAll_shouldReturnAllSubjects() {
	when(subjectDao.findAll()).thenReturn(expectedSubjects);

	assertEquals(expectedSubjects, subjectService.findAll());
    }

    @Test
    void givenCorrectId_onFindById_shouldReturnOptionalWithCorrectSubject() {
	when(subjectDao.findById(1)).thenReturn(Optional.of(expectedSubject1));

	assertEquals(Optional.of(expectedSubject1), subjectService.findById(1));
    }

    @Test
    void givenSubject_onCreate_shouldCallCreate() {
	subjectService.create(expectedSubject1);

	verify(subjectDao).create(expectedSubject1);
    }

    @Test
    void givenSubject_onUpdate_shouldCallUpdate() {
	subjectService.update(expectedSubject1);

	verify(subjectDao).update(expectedSubject1);
    }

    @Test
    void givenSubject_onDelete_shouldCallDelete() {
	subjectService.delete(1);

	verify(subjectDao).delete(1);
    }
}