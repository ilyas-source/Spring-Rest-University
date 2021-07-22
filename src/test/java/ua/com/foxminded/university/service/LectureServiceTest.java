package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.HolidayDaoTest.TestData.expectedHolidays;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLecture1;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLecture2;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLectures;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.lectureToCreate;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.lectureToUpdate;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.lectureWithTeacherOnVacation;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.expectedSubject1;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.expectedSubject4;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Lecture;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureDao lectureDao;
    @Mock
    private HolidayDao holidayDao;
    @Mock
    private StudentDao studentDao;
    @InjectMocks
    private LectureService lectureService;

    @Test
    void onFindAll_shouldReturnCorrectList() {
	when(lectureDao.findAll()).thenReturn(expectedLectures);

	assertEquals(expectedLectures, lectureService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectLecture() {
	when(lectureDao.findById(1)).thenReturn(Optional.of(expectedLecture1));
	Optional<Lecture> expected = Optional.of(expectedLecture1);

	Optional<Lecture> actual = lectureService.findById(1);

	assertEquals(expected, actual);
    }

    @Test
    void givenLectureWithTooSmallClassroom_onCreate_shouldThrowException() {
	when(lectureService.countStudentsInLecture(expectedLecture1)).thenReturn(1000);

	lectureService.create(expectedLecture1);

	verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureOnHoliday_onCreate_shouldThrowException() {
	when(holidayDao.findByDate(expectedLecture1.getDate())).thenReturn(expectedHolidays);

	lectureService.create(expectedLecture1);

	verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureOnSunday_onCreate_shouldThrowException() {
	lectureService.create(lectureToCreate);

	verify(lectureDao, never()).create(lectureToCreate);
    }

    @Test
    void givenLectureWithBusyTeacher_onCreate_shouldThrowException() {
	when(lectureDao.findByDateTimeTeacher(expectedLecture1.getDate(), expectedLecture1.getTimeslot(),
		expectedLecture1.getTeacher())).thenReturn(Optional.of(expectedLecture2));

	lectureService.create(expectedLecture1);

	verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureWithTeacherOnVacation_onCreate_shouldThrowException() {

	lectureService.create(lectureWithTeacherOnVacation);

	verify(lectureDao, never()).create(lectureWithTeacherOnVacation);
    }

    @Test
    void givenLectureWithTeacherCantTeach_onCreate_shouldThrowException() {
	expectedLecture1.setSubject(expectedSubject4);

	lectureService.create(expectedLecture1);

	expectedLecture1.setSubject(expectedSubject1);
	verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureWithBusyGroup_onCreate_shouldThrowException() {
	when(lectureDao.findByDateTime(expectedLecture1.getDate(), expectedLecture1.getTimeslot())).thenReturn(expectedLectures);

	lectureService.create(expectedLecture1);

	verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureWithOccupiedClassroom_onCreate_shouldThrowException() {
	when(lectureDao.findByDateTimeClassroom(expectedLecture1.getDate(), expectedLecture1.getTimeslot(),
		expectedLecture1.getClassroom())).thenReturn(Optional.of(expectedLecture2));

	lectureService.create(expectedLecture1);

	verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenGoodLecture_onCreate_shouldCallDaoCreate() {
	lectureService.create(expectedLecture1);

	verify(lectureDao).create(expectedLecture1);
    }

    @Test
    void givenLectureWithTooSmallClassroom_onUpdate_shouldThrowException() {
	when(lectureService.countStudentsInLecture(expectedLecture1)).thenReturn(1000);

	lectureService.update(expectedLecture1);

	verify(lectureDao, never()).update(expectedLecture1);
    }

    @Test
    void givenLectureOnHoliday_onUpdate_shouldThrowException() {
	when(holidayDao.findByDate(expectedLecture1.getDate())).thenReturn(expectedHolidays);

	lectureService.update(expectedLecture1);

	verify(lectureDao, never()).update(expectedLecture1);
    }

    @Test
    void givenLectureOnSunday_onUpdate_shouldThrowException() {
	lectureService.update(lectureToUpdate);

	verify(lectureDao, never()).update(lectureToUpdate);
    }

    @Test
    void givenLectureWithBusyTeacher_onUpdate_shouldThrowException() {
	when(lectureDao.findByDateTimeTeacher(expectedLecture1.getDate(), expectedLecture1.getTimeslot(),
		expectedLecture1.getTeacher())).thenReturn(Optional.of(expectedLecture2));

	lectureService.update(expectedLecture1);

	verify(lectureDao, never()).update(expectedLecture1);
    }

    @Test
    void givenLectureWithTeacherOnVacation_onUpdate_shouldThrowException() {

	lectureService.update(lectureWithTeacherOnVacation);

	verify(lectureDao, never()).update(expectedLecture1);
    }

    @Test
    void givenLectureWithTeacherCantTeach_onUpdate_shouldThrowException() {
	expectedLecture1.setSubject(expectedSubject4);

	lectureService.update(expectedLecture1);

	expectedLecture1.setSubject(expectedSubject1);
	verify(lectureDao, never()).update(expectedLecture1);
    }

    @Test
    void givenLectureWithBusyGroup_onUpdate_shouldThrowException() {
	when(lectureDao.findByDateTime(expectedLecture1.getDate(), expectedLecture1.getTimeslot())).thenReturn(expectedLectures);

	lectureService.update(expectedLecture1);

	verify(lectureDao, never()).update(expectedLecture1);
    }

    @Test
    void givenLectureWithOccupiedClassroom_onUpdate_shouldThrowException() {
	when(lectureDao.findByDateTimeClassroom(expectedLecture1.getDate(), expectedLecture1.getTimeslot(),
		expectedLecture1.getClassroom())).thenReturn(Optional.of(expectedLecture2));

	lectureService.update(expectedLecture1);

	verify(lectureDao, never()).update(expectedLecture1);
    }

    @Test
    void givenGoodLecture_onUpdate_shouldCallUpdate() {
	lectureService.update(expectedLecture1);

	verify(lectureDao).update(expectedLecture1);
    }

    @Test
    void givenExistingLectureId_onDelete_shouldCallDaoDelete() {
	when(lectureDao.findById(1)).thenReturn(Optional.of(expectedLecture1));

	lectureService.delete(1);

	verify(lectureDao).delete(1);
    }

    @Test
    void givenIncorrectLectureId_onDelete_shouldThrowException() {
	String expected = "Lecture with id=1 not found, nothing to delete";

	Throwable thrown = assertThrows(EntityNotFoundException.class,
		() -> lectureService.delete(1));

	assertEquals(expected, thrown.getMessage());
	verify(lectureDao, never()).delete(1);
    }
}