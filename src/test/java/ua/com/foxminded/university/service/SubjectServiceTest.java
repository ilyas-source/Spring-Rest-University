package ua.com.foxminded.university.service;

import static org.mockito.Mockito.verify;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.expectedSubject1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.SubjectDao;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectDao subjectDao;
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