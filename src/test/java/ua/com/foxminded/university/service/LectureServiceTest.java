package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.HolidayDaoTest.TestData.expectedHolidays;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLecture1;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLecture2;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLectures;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.expectedSubject1;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.expectedSubject4;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.dao.LectureDao;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureDao lectureDao;
    @Mock
    private HolidayDao holidayDao;
    @InjectMocks
    private LectureService lectureService;

    @Test
    void onFindAll_shouldReturnAllLectures() {
	when(lectureDao.findAll()).thenReturn(expectedLectures);

	assertEquals(expectedLectures, lectureService.findAll());
    }

    @Test
    void onFindAll_shouldCallDaoFindAll() {
	lectureService.findAll();

	verify(lectureDao).findAll();
    }

    @Test
    void givenLectureWithTooSmallClassroom_onCreate_shouldNotCallDaoCreate() {
	when(lectureDao.countStudentsInLecture(expectedLecture1)).thenReturn(1000);

	lectureService.create(expectedLecture1);

	verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureOnHoliday_onCreate_shouldNotCallDaoCreate() {
	when(holidayDao.findByDate(expectedLecture1.getDate())).thenReturn(expectedHolidays);

	lectureService.create(expectedLecture1);

	verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureWithBusyTeacher_onCreate_shouldNotCallDaoCreate() {
	when(lectureDao.findByDateTimeTeacher(expectedLecture1.getDate(), expectedLecture1.getTimeslot(),
		expectedLecture1.getTeacher())).thenReturn(Optional.of(expectedLecture2));

	lectureService.create(expectedLecture1);

	verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureWithTeacherOnVacation_onCreate_shouldNotCallDaoCreate() {

	LocalDate dateBackup = expectedLecture1.getDate();
	expectedLecture1.setDate(expectedLecture1.getTeacher().getVacations().get(1).getStartDate());

	lectureService.create(expectedLecture1);

	expectedLecture1.setDate(dateBackup);
	verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureWithTeacherCantTeach_onCreate_shouldNotCallDaoCreate() {
	expectedLecture1.setSubject(expectedSubject4);

	lectureService.create(expectedLecture1);

	expectedLecture1.setSubject(expectedSubject1);
	verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureWithBusyGroup_onCreate_shouldNotCallDaoCreate() {
	when(lectureDao.findByDateTime(expectedLecture1.getDate(), expectedLecture1.getTimeslot())).thenReturn(expectedLectures);

	lectureService.create(expectedLecture1);

	verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureWithBusyClassroom_onCreate_shouldNotCallDaoCreate() {
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
    void givenLecture_onUpdate_shouldCallUpdate() {
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
    void givenNonExistentLectureId_onDelete_shouldNotCallDaoDelete() {
	lectureService.delete(100);

	verify(lectureDao, never()).delete(100);
    }
}