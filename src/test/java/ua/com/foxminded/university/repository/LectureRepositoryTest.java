package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Timeslot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.repository.ClassroomRepositoryTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.repository.ClassroomRepositoryTest.TestData.expectedClassroom2;
import static ua.com.foxminded.university.repository.GroupRepositoryTest.TestData.expectedGroup1;
import static ua.com.foxminded.university.repository.GroupRepositoryTest.TestData.expectedGroup2;
import static ua.com.foxminded.university.repository.LectureRepositoryTest.TestData.*;
import static ua.com.foxminded.university.repository.SubjectRepositoryTest.TestData.expectedSubject1;
import static ua.com.foxminded.university.repository.SubjectRepositoryTest.TestData.expectedSubject2;
import static ua.com.foxminded.university.repository.TeacherRepositoryTest.TestData.expectedTeacher1;
import static ua.com.foxminded.university.repository.TeacherRepositoryTest.TestData.expectedTeacher2;

@DataJpaTest
public class LectureRepositoryTest {

    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    TeacherRepository teacherRepository;

    @Test
    void givenClassroom_onFindByClassroom_shouldReturnCorrectListOfLectures() {
        List<Lecture> expected = new ArrayList<>(List.of(expectedLecture1));
        teacherRepository.findAll();

        List<Lecture> actual = lectureRepository.findByClassroom(expectedClassroom1);

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacher_onFindByTeacher_shouldReturnCorrectListOfLectures() {
        List<Lecture> expected = new ArrayList<>(List.of(expectedLecture1));
        teacherRepository.findAll();

        List<Lecture> actual = lectureRepository.findByTeacher(expectedTeacher1);

        assertEquals(expected, actual);
    }

    @Test
    void givenSubject_onFindBySubject_shouldReturnCorrectListOfLectures() {
        List<Lecture> expected = new ArrayList<>(List.of(expectedLecture1));
        teacherRepository.findAll();

        List<Lecture> actual = lectureRepository.findBySubject(expectedSubject1);

        assertEquals(expected, actual);
    }

    @Test
    void givenTimeslot_onFindByTimeslot_shouldReturnCorrectListOfLectures() {
        List<Lecture> expected = new ArrayList<>(List.of(expectedLecture1));
        teacherRepository.findAll();

        List<Lecture> actual = lectureRepository.findByTimeslot(expectedTimeslot1);

        assertEquals(expected, actual);
    }

    @Test
    void givenDateAndTimeslot_onFindByDateAndTimeslot_shouldReturnCorrectListOfLectures() {
        List<Lecture> expected = new ArrayList<>(List.of(expectedLecture1));
        teacherRepository.findAll();

        List<Lecture> actual = lectureRepository.findByDateAndTimeslot(LocalDate.of(2020, 1, 1), expectedTimeslot1);

        assertEquals(expected, actual);
    }

    @Test
    void givenDateTimeslotAndClassroom_onFindByDateTimeClassroom_shouldReturnOptionalWithCorrectLecture() {
        Optional<Lecture> expected = Optional.of(expectedLecture1);
        teacherRepository.findAll();

        Optional<Lecture> actual = lectureRepository
                .findByDateAndTimeslotAndClassroom(LocalDate.of(2020, 1, 1),
                        expectedTimeslot1, expectedClassroom1);

        assertEquals(expected, actual);
    }

    @Test
    void givenWrongData_onFindByDateAndTimeslotAndClassroom_shouldReturnOptionalEmpty() {
        Optional<Lecture> actual = lectureRepository
                .findByDateAndTimeslotAndClassroom(LocalDate.of(2025, 1, 1),
                        expectedTimeslot1, expectedClassroom1);

        assertEquals(Optional.empty(), actual);
    }

    @Test
    void givenDateTimeslotAndTeacher_onFindByDateAndTimeslotAndTeacher_shouldReturnOptionalWithCorrectLecture() {
        Optional<Lecture> expected = Optional.of(expectedLecture1);
        teacherRepository.findAll();

        Optional<Lecture> actual = lectureRepository
                .findByDateAndTimeslotAndTeacher(LocalDate.of(2020, 1, 1),
                        expectedTimeslot1, expectedTeacher1);

        assertEquals(expected, actual);
    }

    @Test
    void givenWrongData_onFindByDateTimeTeacher_shouldReturnOptionalEmpty() {
        teacherRepository.findAll();
        Optional<Lecture> actual = lectureRepository
                .findByDateAndTimeslotAndTeacher(LocalDate.of(2025, 1, 1),
                        expectedTimeslot1, expectedTeacher1);

        assertEquals(Optional.empty(), actual);
    }

    @Test
    void givenTeacherAndDates_onFindByTeacherAndPeriod_shouldReturnCorrectListOfLectures() {
        var startDate = LocalDate.of(2020, 1, 1);
        var endDate = LocalDate.of(2020, 1, 3);
        List<Lecture> expected = new ArrayList<>(List.of(expectedLecture1));
        teacherRepository.findAll();

        var actual = lectureRepository.findByTeacherAndDateBetween(expectedTeacher1, startDate, endDate);

        assertEquals(expected, actual);
    }

    @Test
    void givenGroupAndDates_onFindByGroupAndPeriod_shouldReturnCorrectListOfLectures() {
        var startDate = LocalDate.of(2020, 1, 1);
        var endDate = LocalDate.of(2020, 1, 3);
        teacherRepository.findAll();

        var actual = lectureRepository.findByGroupsAndDateBetween(expectedGroup1, startDate, endDate);

        assertEquals(expectedLectures, actual);
    }

    interface TestData {
        Timeslot expectedTimeslot1 = new Timeslot(1, LocalTime.of(9, 0), LocalTime.of(9, 45));
        Timeslot expectedTimeslot2 = new Timeslot(2, LocalTime.of(10, 0), LocalTime.of(10, 45));
        Set<Group> expectedGroups1 = new HashSet<>(Arrays.asList(expectedGroup1, expectedGroup2));
        Set<Group> expectedGroups2 = new HashSet<>(List.of(expectedGroup1));

        Lecture expectedLecture1 = Lecture.builder().date(LocalDate.of(2020, 1, 1)).subject(expectedSubject1)
                .id(1).timeslot(expectedTimeslot1).groups(expectedGroups1)
                .teacher(expectedTeacher1).classroom(expectedClassroom1).build();

        Lecture expectedLecture2 = Lecture.builder().date(LocalDate.of(2020, 1, 2)).subject(expectedSubject2)
                .id(2).timeslot(expectedTimeslot2).groups(expectedGroups2)
                .teacher(expectedTeacher2).classroom(expectedClassroom2).build();

        List<Lecture> expectedLectures = new ArrayList<>(Arrays.asList(expectedLecture1, expectedLecture2));
    }
}
