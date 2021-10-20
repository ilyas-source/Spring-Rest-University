package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.exception.*;
import ua.com.foxminded.university.model.Lecture;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.dao.HolidayDaoTest.TestData.expectedHolidays;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.StudentDaoTest.TestData.expectedStudent1;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.expectedTimeslot1;
import static ua.com.foxminded.university.service.LectureServiceTest.TestData.lectureWithTeacherOnVacation;
import static ua.com.foxminded.university.service.LectureServiceTest.TestData.lecturesToReplaceTeacher;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureDao lectureDao;
    @Mock
    private HolidayDao holidayDao;
    @Mock
    private TeacherService teacherService;
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
    void givenId_onGetById_shouldReturnLecture() {
        when(lectureService.findById(1)).thenReturn(Optional.of(expectedLecture1));

        assertEquals(expectedLecture1, lectureService.getById(1));
    }

    @Test
    void givenLectureWithTooSmallClassroom_onCreate_shouldThrowException() {
        String expected = "Classroom too small: required 501, but was 500";
        when(lectureService.countStudentsInLecture(expectedLecture1)).thenReturn(501);

        Throwable thrown = assertThrows(ClassroomInvalidCapacityException.class,
                () -> lectureService.create(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureOnHoliday_onCreate_shouldThrowException() {
        String expected = "Can't schedule lecture to a holiday";
        when(holidayDao.findByDate(expectedLecture1.getDate())).thenReturn(expectedHolidays);

        Throwable thrown = assertThrows(LectureOnHolidayException.class,
                () -> lectureService.create(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureOnSunday_onCreate_shouldThrowException() {
        String expected = "Can't schedule lecture to a weekend";
        Throwable thrown = assertThrows(LectureOnWeekendException.class,
                () -> lectureService.create(lectureToCreate));

        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).create(lectureToCreate);
    }

    @Test
    void givenLectureWithBusyTeacher_onCreate_shouldThrowException() {
        String expected = "Teacher Adam Smith will be reading another lecture";
        when(lectureDao.findByDateTimeTeacher(expectedLecture1.getDate(), expectedLecture1.getTimeslot(),
                expectedLecture1.getTeacher())).thenReturn(Optional.of(expectedLecture2));

        Throwable thrown = assertThrows(TeacherBusyException.class,
                () -> lectureService.create(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureWithTeacherOnVacation_onCreate_shouldThrowException() {
        String expected = "Teacher Adam Smith will be on a vacation, can't schedule lecture";
        Throwable thrown = assertThrows(TeacherOnVacationException.class,
                () -> lectureService.create(lectureWithTeacherOnVacation));

        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).create(lectureWithTeacherOnVacation);
    }

    @Test
    void givenLectureWithTeacherCantTeach_onCreate_shouldThrowException() {
        String expected = "Teacher Adam Smith can't teach Test Radiology";
        expectedLecture1.setSubject(expectedSubject4);

        Throwable thrown = assertThrows(TeacherCannotTeachSubject.class,
                () -> lectureService.create(expectedLecture1));

        expectedLecture1.setSubject(expectedSubject1);
        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureWithBusyGroup_onCreate_shouldThrowException() {
        String expected = "Group(s) will be attending another lecture";
        when(lectureDao.findByDateTime(expectedLecture1.getDate(), expectedLecture1.getTimeslot())).thenReturn(expectedLectures);

        Throwable thrown = assertThrows(GroupBusyException.class,
                () -> lectureService.create(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenLectureWithOccupiedClassroom_onCreate_shouldThrowException() {
        String expected = "Classroom Big physics auditory is occupied at this day and time";
        when(lectureDao.findByDateTimeClassroom(expectedLecture1.getDate(), expectedLecture1.getTimeslot(),
                expectedLecture1.getClassroom())).thenReturn(Optional.of(expectedLecture2));

        Throwable thrown = assertThrows(ClassroomOccupiedException.class,
                () -> lectureService.create(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).create(expectedLecture1);
    }

    @Test
    void givenGoodLecture_onCreate_shouldCallDaoCreate() {
        lectureService.create(expectedLecture1);

        verify(lectureDao).create(expectedLecture1);
    }

    @Test
    void givenLectureWithTooSmallClassroom_onUpdate_shouldThrowException() {
        String expected = "Classroom too small: required 1000, but was 500";
        when(lectureService.countStudentsInLecture(expectedLecture1)).thenReturn(1000);

        Throwable thrown = assertThrows(ClassroomInvalidCapacityException.class,
                () -> lectureService.update(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).update(expectedLecture1);
    }

    @Test
    void givenLectureOnHoliday_onUpdate_shouldThrowException() {
        String expected = "Can't schedule lecture to a holiday";
        when(holidayDao.findByDate(expectedLecture1.getDate())).thenReturn(expectedHolidays);

        Throwable thrown = assertThrows(LectureOnHolidayException.class,
                () -> lectureService.update(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).update(expectedLecture1);
    }

    @Test
    void givenLectureOnSunday_onUpdate_shouldThrowException() {
        String expected = "Can't schedule lecture to a weekend";

        Throwable thrown = assertThrows(LectureOnWeekendException.class,
                () -> lectureService.update(lectureToUpdate));

        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).update(lectureToUpdate);
    }

    @Test
    void givenLectureWithBusyTeacher_onUpdate_shouldThrowException() {
        String expected = "Teacher Adam Smith will be reading another lecture";
        when(lectureDao.findByDateTimeTeacher(expectedLecture1.getDate(), expectedLecture1.getTimeslot(),
                expectedLecture1.getTeacher())).thenReturn(Optional.of(expectedLecture2));

        Throwable thrown = assertThrows(TeacherBusyException.class,
                () -> lectureService.update(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).update(expectedLecture1);
    }

    @Test
    void givenLectureWithTeacherOnVacation_onUpdate_shouldThrowException() {
        String expected = "Teacher Adam Smith will be on a vacation, can't schedule lecture";

        Throwable thrown = assertThrows(TeacherOnVacationException.class,
                () -> lectureService.update(lectureWithTeacherOnVacation));

        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).update(expectedLecture1);
    }

    @Test
    void givenLectureWithTeacherCantTeach_onUpdate_shouldThrowException() {
        String expected = "Teacher Adam Smith can't teach Test Radiology";
        expectedLecture1.setSubject(expectedSubject4);

        Throwable thrown = assertThrows(TeacherCannotTeachSubject.class,
                () -> lectureService.update(expectedLecture1));

        expectedLecture1.setSubject(expectedSubject1);
        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).update(expectedLecture1);
    }

    @Test
    void givenLectureWithBusyGroup_onUpdate_shouldThrowException() {
        String expected = "Group(s) will be attending another lecture";
        when(lectureDao.findByDateTime(expectedLecture1.getDate(), expectedLecture1.getTimeslot())).thenReturn(expectedLectures);

        Throwable thrown = assertThrows(GroupBusyException.class,
                () -> lectureService.update(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).update(expectedLecture1);
    }

    @Test
    void givenLectureWithOccupiedClassroom_onUpdate_shouldThrowException() {
        String expected = "Classroom Big physics auditory is occupied at this day and time";
        when(lectureDao.findByDateTimeClassroom(expectedLecture1.getDate(), expectedLecture1.getTimeslot(),
                expectedLecture1.getClassroom())).thenReturn(Optional.of(expectedLecture2));

        Throwable thrown = assertThrows(ClassroomOccupiedException.class,
                () -> lectureService.update(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
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
        String expected = "Lecture id:1 not found, nothing to delete";

        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> lectureService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureDao, never()).delete(1);
    }

    @Test
    void givenTeacherAndDates_onFindByTeacherAndPeriod_shouldCallDaoFindByTeacherAndPeriod() {
        LocalDate start = LocalDate.of(2000, 1, 1);
        LocalDate end = LocalDate.of(2001, 1, 1);
        when(lectureDao.findByTeacherAndPeriod(expectedTeacher1, start, end))
                .thenReturn(expectedLectures);

        var actual = lectureService.findByTeacherAndPeriod(expectedTeacher1, start, end);

        assertEquals(expectedLectures, actual);
        verify(lectureDao).findByTeacherAndPeriod(expectedTeacher1, start, end);
    }

    @Test
    void givenStudentAndDates_onFindByStudentAndPeriod_shouldCallDaoFindByStudentAndPeriod() {
        LocalDate start = LocalDate.of(2000, 1, 1);
        LocalDate end = LocalDate.of(2001, 1, 1);
        when(lectureDao.findByStudentAndPeriod(expectedStudent1, start, end))
                .thenReturn(expectedLectures);

        var actual = lectureService.findByStudentAndPeriod(expectedStudent1, start, end);

        assertEquals(expectedLectures, actual);
        verify(lectureDao).findByStudentAndPeriod(expectedStudent1, start, end);
    }

    @Test
    void givenTeacherAndDates_onReplaceTeacher_shouldReplaceTeacher() {
        var startDate = LocalDate.of(2000, 1, 1);
        var endDate = LocalDate.of(2000, 1, 3);
        when(lectureDao.findByTeacherAndPeriod(expectedTeacher2, startDate, endDate))
                .thenReturn(lecturesToReplaceTeacher);
        when(teacherService.getReplacementTeachers(lectureToReplaceTeacher)).thenReturn(expectedTeachers);

        lectureService.replaceTeacher(expectedTeacher2, startDate, endDate);

        assertThat(lectureToReplaceTeacher.getTeacher().equals(expectedTeacher1));
        verify(lectureDao).update(lectureToReplaceTeacher);
    }

//    @Transactional
//    public void replaceTeacher(Teacher teacher, LocalDate start, LocalDate end) {
//        List<Lecture> lectures = lectureDao.findByTeacherAndPeriod(teacher, start, end);
//        logger.debug("Found {} lectures for this teacher and dates: {}", lectures.size(), lectures);
//
//        for (Lecture lecture : lectures) {
//            logger.debug("Trying to replace teacher in {}", lecture);
//
//            List<Teacher> replacementTeachers = teacherService.getReplacementTeachers(lecture);
//            logger.debug("Found {} suitable teachers", replacementTeachers.size());
//
//            if (replacementTeachers.size() == 0) {
//                throw new CannotReplaceTeacherException(String.format("Can't replace teacher in %s: no suitable teachers found", lecture));
//            }
//            Teacher goodTeacher = replacementTeachers.get(0);
//            logger.debug("Found good candidate: {} {}", goodTeacher.getFirstName(), goodTeacher.getLastName());
//            lecture.setTeacher(goodTeacher);
//            lectureDao.update(lecture);
//        }
//    }

    interface TestData {
        Lecture lectureWithTeacherOnVacation = Lecture.builder().date(LocalDate.of(2000, 1, 1)).subject(expectedSubject1)
                .id(1).timeslot(expectedTimeslot1).groups(expectedGroups1)
                .teacher(expectedTeacher1).classroom(expectedClassroom1).build();

        Lecture lectureToReplaceTeacher = Lecture.builder().date(LocalDate.of(2021, 1, 1)).
                subject(expectedSubject3).timeslot(expectedTimeslot1).teacher(expectedTeacher2).build();

        List<Lecture> lecturesToReplaceTeacher = new ArrayList<>(Arrays.asList(lectureToReplaceTeacher));
    }
}