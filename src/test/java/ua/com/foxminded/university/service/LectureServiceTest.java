package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.jdbc.JdbcLectureDao;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private JdbcLectureDao lectureDao;
    @InjectMocks
    private LectureService lectureService;

    @Test
    void onFindAll_shouldReturnAllLectures() {
	when(lectureDao.findAll()).thenReturn(expectedLectures);

	assertEquals(expectedLectures, lectureService.findAll());
    }

    @Test
    void givenCorrectId_onFindById_shouldReturnOptionalWithCorrectLecture() {
	when(lectureDao.findById(1)).thenReturn(Optional.of(expectedLecture1));

	assertEquals(Optional.of(expectedLecture1), lectureService.findById(1));
    }

    @Test
    void givenLecture_onCreate_shouldCallCreate() {
	lectureService.create(expectedLecture1);

	verify(lectureDao).create(expectedLecture1);
    }

    @Test
    void givenLecture_onUpdate_shouldCallUpdate() {
	lectureService.update(expectedLecture1);

	verify(lectureDao).update(expectedLecture1);
    }

    @Test
    void givenLecture_onDelete_shouldCallDelete() {
	lectureService.delete(1);

	verify(lectureDao).delete(1);
    }
}