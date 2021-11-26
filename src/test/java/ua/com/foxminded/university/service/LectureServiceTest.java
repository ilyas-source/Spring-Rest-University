package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.exception.*;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.repository.HolidayRepository;
import ua.com.foxminded.university.repository.LectureRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.service.ClassroomServiceTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.service.ClassroomServiceTest.TestData.expectedClassroom2;
import static ua.com.foxminded.university.service.GroupServiceTest.TestData.*;
import static ua.com.foxminded.university.service.HolidayServiceTest.TestData.expectedHolidays;
import static ua.com.foxminded.university.service.LectureServiceTest.TestData.lectureToReplaceTeacher;
import static ua.com.foxminded.university.service.LectureServiceTest.TestData.*;
import static ua.com.foxminded.university.service.StudentServiceTest.TestData.expectedStudent1;
import static ua.com.foxminded.university.service.SubjectServiceTest.TestData.*;
import static ua.com.foxminded.university.service.TeacherServiceTest.TestData.*;
import static ua.com.foxminded.university.service.TimeslotServiceTest.TestData.expectedTimeslot1;
import static ua.com.foxminded.university.service.TimeslotServiceTest.TestData.expectedTimeslot2;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;
    @Mock
    private HolidayRepository holidayRepository;
    @Mock
    private TeacherService teacherService;
    @InjectMocks
    private LectureService lectureService;

    @Test
    void onFindAll_shouldReturnCorrectList() {
        when(lectureRepository.findAll()).thenReturn(expectedLectures);

        assertEquals(expectedLectures, lectureService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectLecture() {
        when(lectureRepository.findById(1)).thenReturn(Optional.of(expectedLecture1));
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
        verify(lectureRepository, never()).save(expectedLecture1);
    }

    @Test
    void givenLectureOnHoliday_onCreate_shouldThrowException() {
        String expected = "Can't schedule lecture to a holiday";
        when(holidayRepository.findByDate(expectedLecture1.getDate())).thenReturn(expectedHolidays);

        Throwable thrown = assertThrows(LectureOnHolidayException.class,
                () -> lectureService.create(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(expectedLecture1);
    }

    @Test
    void givenLectureOnSunday_onCreate_shouldThrowException() {
        String expected = "Can't schedule lecture to a weekend";
        Throwable thrown = assertThrows(LectureOnWeekendException.class,
                () -> lectureService.create(lectureToCreate));

        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(lectureToCreate);
    }

    @Test
    void givenLectureWithBusyTeacher_onCreate_shouldThrowException() {
        String expected = "Teacher Adam Smith will be reading another lecture";
        when(lectureRepository.findByDateAndTimeslotAndTeacher(expectedLecture1.getDate(), expectedLecture1.getTimeslot(),
                expectedLecture1.getTeacher())).thenReturn(Optional.of(expectedLecture2));

        Throwable thrown = assertThrows(TeacherBusyException.class,
                () -> lectureService.create(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(expectedLecture1);
    }

    @Test
    void givenLectureWithTeacherOnVacation_onCreate_shouldThrowException() {
        String expected = "Teacher Adam Smith will be on a vacation, can't schedule lecture";
        Throwable thrown = assertThrows(TeacherOnVacationException.class,
                () -> lectureService.create(lectureWithTeacherOnVacation));

        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(lectureWithTeacherOnVacation);
    }

    @Test
    void givenLectureWithTeacherCantTeach_onCreate_shouldThrowException() {
        String expected = "Teacher Adam Smith can't teach Test Radiology";
        expectedLecture1.setSubject(expectedSubject4);

        Throwable thrown = assertThrows(TeacherCannotTeachSubject.class,
                () -> lectureService.create(expectedLecture1));

        expectedLecture1.setSubject(expectedSubject1);
        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(expectedLecture1);
    }

    @Test
    void givenLectureWithBusyGroup_onCreate_shouldThrowException() {
        String expected = "Group(s) will be attending another lecture";
        when(lectureRepository.findByDateAndTimeslot(expectedLecture1.getDate(), expectedLecture1.getTimeslot())).thenReturn(expectedLectures);

        Throwable thrown = assertThrows(GroupBusyException.class,
                () -> lectureService.create(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(expectedLecture1);
    }

    @Test
    void givenLectureWithOccupiedClassroom_onCreate_shouldThrowException() {
        String expected = "Classroom Big physics auditory is occupied at this day and time";
        when(lectureRepository.findByDateAndTimeslotAndClassroom(expectedLecture1.getDate(), expectedLecture1.getTimeslot(),
                expectedLecture1.getClassroom())).thenReturn(Optional.of(expectedLecture2));

        Throwable thrown = assertThrows(ClassroomOccupiedException.class,
                () -> lectureService.create(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(expectedLecture1);
    }

    @Test
    void givenGoodLecture_onCreate_shouldCallRepositoryCreate() {
        lectureService.create(expectedLecture1);

        verify(lectureRepository).save(expectedLecture1);
    }

    @Test
    void givenLectureWithTooSmallClassroom_onUpdate_shouldThrowException() {
        String expected = "Classroom too small: required 1000, but was 500";
        when(lectureService.countStudentsInLecture(expectedLecture1)).thenReturn(1000);

        Throwable thrown = assertThrows(ClassroomInvalidCapacityException.class,
                () -> lectureService.update(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(expectedLecture1);
    }

    @Test
    void givenLectureOnHoliday_onUpdate_shouldThrowException() {
        String expected = "Can't schedule lecture to a holiday";
        when(holidayRepository.findByDate(expectedLecture1.getDate())).thenReturn(expectedHolidays);

        Throwable thrown = assertThrows(LectureOnHolidayException.class,
                () -> lectureService.update(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(expectedLecture1);
    }

    @Test
    void givenLectureOnSunday_onUpdate_shouldThrowException() {
        String expected = "Can't schedule lecture to a weekend";

        Throwable thrown = assertThrows(LectureOnWeekendException.class,
                () -> lectureService.update(lectureToUpdate));

        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(lectureToUpdate);
    }

    @Test
    void givenLectureWithBusyTeacher_onUpdate_shouldThrowException() {
        String expected = "Teacher Adam Smith will be reading another lecture";
        when(lectureRepository.findByDateAndTimeslotAndTeacher(expectedLecture1.getDate(), expectedLecture1.getTimeslot(),
                expectedLecture1.getTeacher())).thenReturn(Optional.of(expectedLecture2));

        Throwable thrown = assertThrows(TeacherBusyException.class,
                () -> lectureService.update(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(expectedLecture1);
    }

    @Test
    void givenLectureWithTeacherOnVacation_onUpdate_shouldThrowException() {
        String expected = "Teacher Adam Smith will be on a vacation, can't schedule lecture";

        Throwable thrown = assertThrows(TeacherOnVacationException.class,
                () -> lectureService.update(lectureWithTeacherOnVacation));

        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(expectedLecture1);
    }

    @Test
    void givenLectureWithTeacherCantTeach_onUpdate_shouldThrowException() {
        String expected = "Teacher Adam Smith can't teach Test Radiology";
        expectedLecture1.setSubject(expectedSubject4);

        Throwable thrown = assertThrows(TeacherCannotTeachSubject.class,
                () -> lectureService.update(expectedLecture1));

        expectedLecture1.setSubject(expectedSubject1);
        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(expectedLecture1);
    }

    @Test
    void givenLectureWithBusyGroup_onUpdate_shouldThrowException() {
        String expected = "Group(s) will be attending another lecture";
        when(lectureRepository.findByDateAndTimeslot(expectedLecture1.getDate(), expectedLecture1.getTimeslot())).thenReturn(expectedLectures);

        Throwable thrown = assertThrows(GroupBusyException.class,
                () -> lectureService.update(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(expectedLecture1);
    }

    @Test
    void givenLectureWithOccupiedClassroom_onUpdate_shouldThrowException() {
        String expected = "Classroom Big physics auditory is occupied at this day and time";
        when(lectureRepository.findByDateAndTimeslotAndClassroom(expectedLecture1.getDate(), expectedLecture1.getTimeslot(),
                expectedLecture1.getClassroom())).thenReturn(Optional.of(expectedLecture2));

        Throwable thrown = assertThrows(ClassroomOccupiedException.class,
                () -> lectureService.update(expectedLecture1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).save(expectedLecture1);
    }

    @Test
    void givenGoodLecture_onUpdate_shouldCallUpdate() {
        lectureService.update(expectedLecture1);

        verify(lectureRepository).save(expectedLecture1);
    }

    @Test
    void givenExistingLectureId_onDelete_shouldCallRepositoryDelete() {
        when(lectureRepository.findById(1)).thenReturn(Optional.of(expectedLecture1));

        lectureService.delete(1);

        verify(lectureRepository).delete(expectedLecture1);
    }

    @Test
    void givenIncorrectLectureId_onDelete_shouldThrowException() {
        String expected = "Lecture id:1 not found, nothing to delete";

        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> lectureService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(lectureRepository, never()).delete(any());
    }

    @Test
    void givenTeacherAndDates_onFindByTeacherAndPeriod_shouldCallRepositoryFindByTeacherAndPeriod() {
        LocalDate start = LocalDate.of(2000, 1, 1);
        LocalDate end = LocalDate.of(2001, 1, 1);
        when(lectureRepository.findByTeacherAndDateBetween(expectedTeacher1, start, end))
                .thenReturn(expectedLectures);

        var actual = lectureService.findByTeacherAndPeriod(expectedTeacher1, start, end);

        assertEquals(expectedLectures, actual);
        verify(lectureRepository).findByTeacherAndDateBetween(expectedTeacher1, start, end);
    }

    @Test
    void givenStudentAndDates_onFindByStudentAndPeriod_shouldCallRepositoryFindByStudentAndPeriod() {
        LocalDate start = LocalDate.of(2000, 1, 1);
        LocalDate end = LocalDate.of(2001, 1, 1);
        when(lectureRepository.findByGroupsAndDateBetween(expectedGroup1, start, end))
                .thenReturn(expectedLectures);

        var actual = lectureService.findByStudentAndPeriod(expectedStudent1, start, end);

        assertEquals(expectedLectures, actual);
        verify(lectureRepository).findByGroupsAndDateBetween(expectedGroup1, start, end);
    }

    @Test
    void givenTeacherAndDates_onReplaceTeacher_shouldReplaceTeacher() {
        var startDate = LocalDate.of(2000, 1, 1);
        var endDate = LocalDate.of(2000, 1, 3);
        when(lectureRepository.findByTeacherAndDateBetween(expectedTeacher2, startDate, endDate))
                .thenReturn(lecturesToReplaceTeacher);
        when(teacherService.getReplacementTeachers(lectureToReplaceTeacher)).thenReturn(expectedTeachers);

        lectureService.replaceTeacher(expectedTeacher2, startDate, endDate);

        assertThat(lectureToReplaceTeacher.getTeacher().equals(expectedTeacher1));
        verify(lectureRepository).save(lectureToReplaceTeacher);
    }

    interface TestData {
        Lecture lectureWithTeacherOnVacation = Lecture.builder().date(LocalDate.of(2000, 1, 1)).subject(expectedSubject1)
                .id(1).timeslot(expectedTimeslot1).groups(expectedGroups1)
                .teacher(expectedTeacher1).classroom(expectedClassroom1).build();

        Lecture lectureToReplaceTeacher = Lecture.builder().date(LocalDate.of(2021, 1, 1)).
                subject(expectedSubject3).timeslot(expectedTimeslot1).teacher(expectedTeacher2).build();

        List<Lecture> lecturesToReplaceTeacher = new ArrayList<>(List.of(lectureToReplaceTeacher));

        Lecture expectedLecture1 = Lecture.builder().date(LocalDate.of(2020, 1, 1)).subject(expectedSubject1)
                .id(1).timeslot(expectedTimeslot1).groups(expectedGroups1)
                .teacher(expectedTeacher1).classroom(expectedClassroom1).build();

        Lecture expectedLecture2 = Lecture.builder().date(LocalDate.of(2020, 1, 2)).subject(expectedSubject2)
                .id(2).timeslot(expectedTimeslot2).groups(expectedGroups2)
                .teacher(expectedTeacher2).classroom(expectedClassroom2).build();

        List<Lecture> expectedLectures = new ArrayList<>(Arrays.asList(expectedLecture1, expectedLecture2));

        Lecture lectureToCreate = Lecture.builder().date(LocalDate.of(2010, 10, 10)).subject(expectedSubject1)
                .timeslot(expectedTimeslot1).groups(testGroups).teacher(expectedTeacher1)
                .classroom(expectedClassroom1).id(3).build();

        Lecture lectureToUpdate = Lecture.builder().date(LocalDate.of(2010, 10, 10)).subject(expectedSubject1)
                .timeslot(expectedTimeslot1).groups(expectedGroupsAfterUpdate).teacher(expectedTeacher1)
                .classroom(expectedClassroom1).id(2).build();
    }
}