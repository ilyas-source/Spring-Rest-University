package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.HolidayDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.remoting.soap.SoapFaultException;

import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

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
    void givenCorrectId_onFindById_shouldReturnOptionalWithCorrectLecture() {
	when(lectureDao.findById(1)).thenReturn(Optional.of(expectedLecture1));

	assertEquals(Optional.of(expectedLecture1), lectureService.findById(1));
    }

    @Test
    void givenLectureWithTooSmallClassroom_onCreate_shouldThrowException() throws Exception {
	String expected = "Required minimum classroom capacity 1000, but was 500";
	when(lectureDao.countStudentsInLecture(expectedLecture1)).thenReturn(1000);

	Throwable thrown = assertThrows(Exception.class, () -> {
	    lectureService.create(expectedLecture1);
	});

	assertEquals(expected, thrown.getMessage());
    }

    @Test
    void givenLectureOnHoliday_onCreate_shouldThrowException() throws Exception {
	String expected = "Can't schedule lecture to a holiday";
	when(holidayDao.findAll()).thenReturn(expectedHolidays);
	expectedLecture1.setDate(LocalDate.of(2000, 3, 8));

	Throwable thrown = assertThrows(Exception.class, () -> {
	    lectureService.create(expectedLecture1);
	});

	assertEquals(expected, thrown.getMessage());
    }

    @Test
    void givenLectureWithBusyTeacher_onCreate_shouldThrowException() throws Exception {
	String expected = "Teacher is busy, can't assign this lecture";
	when(lectureDao.findByTeacher(expectedTeacher1)).thenReturn(expectedLectures);

	Throwable thrown = assertThrows(Exception.class, () -> {
	    lectureService.create(expectedLecture1);
	});

	assertEquals(expected, thrown.getMessage());
    }

    @Test
    void givenLectureWithTeacherOnVacation_onCreate_shouldThrowException() throws Exception {
	String expected = "Teacher is on vacation, can't assign this lecture";

	Throwable thrown = assertThrows(Exception.class, () -> {
	    lectureService.create(expectedLecture1);
	});

	assertEquals(expected, thrown.getMessage());
    }

    @Test
    void givenLectureWithTeacherCantTeach_onCreate_shouldThrowException() throws Exception {
	String expected = "Adam Smith can't teach Test Radiology, can't assign lecture";
	expectedLecture1.setSubject(expectedSubject4);
	expectedLecture1.setDate(LocalDate.of(2020, 1, 1));

	System.out.println(expectedLecture1);

	Throwable thrown = assertThrows(Exception.class, () -> {
	    lectureService.create(expectedLecture1);
	});

	assertEquals(expected, thrown.getMessage());
    }

//    private void verifyTeacherCanTeach(Lecture lecture) throws Exception {
//	Teacher teacher = lecture.getTeacher();
//	Subject subject = lecture.getSubject();
//	if (!teacher.getSubjects().contains(subject)) {
//	    throw new Exception(String.format("%s %s can't teach %s, can't assign lecture", teacher.getFirstName(),
//		    teacher.getLastName(), subject.getName()));
//	}
//    }
    @Test
    void givenGoodLecture_onCreate_shouldCallCreate() throws Exception {
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