package ua.com.foxminded.university.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.hibernate.HibernateLectureDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ua.com.foxminded.university.dao.HibernateClassroomDaoTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.dao.HibernateClassroomDaoTest.TestData.expectedClassroom2;
import static ua.com.foxminded.university.dao.HibernateGroupDaoTest.TestData.expectedGroup1;
import static ua.com.foxminded.university.dao.HibernateGroupDaoTest.TestData.expectedGroup2;
import static ua.com.foxminded.university.dao.HibernateLectureDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.HibernateStudentDaoTest.TestData.expectedStudent1;
import static ua.com.foxminded.university.dao.HibernateSubjectDaoTest.TestData.expectedSubject1;
import static ua.com.foxminded.university.dao.HibernateSubjectDaoTest.TestData.expectedSubject2;
import static ua.com.foxminded.university.dao.HibernateTeacherDaoTest.TestData.expectedTeacher1;
import static ua.com.foxminded.university.dao.HibernateTeacherDaoTest.TestData.expectedTeacher2;
import static ua.com.foxminded.university.dao.HibernateTimeslotDaoTest.TestData.expectedTimeslot1;
import static ua.com.foxminded.university.dao.HibernateTimeslotDaoTest.TestData.expectedTimeslot2;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class HibernateLectureDaoTest {

    @Autowired
    private HibernateLectureDao lectureDao;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    void givenNewLecture_onCreate_shouldCreateLecture() {
        var actual = hibernateTemplate.get(Lecture.class, 3);
        assertNull(actual);

        lectureDao.create(lectureToCreate);

        actual = hibernateTemplate.get(Lecture.class, 3);
        assertEquals(lectureToCreate, actual);
    }

    @Test
    void givenCorrectLectureId_onFindById_shouldReturnOptionalWithCorrectLecture() {
        var expected = Optional.of(expectedLecture2);

        var actual = lectureDao.findById(2);

        assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectLectureId_onFindById_shouldReturnEmptyOptional() {
        Optional<Lecture> expected = Optional.empty();

        var actual = lectureDao.findById(5);

        assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasLectures_onFindAll_shouldReturnCorrectListOfLectures() {
        assertEquals(expectedLectures, lectureDao.findAll());
    }

    @Test
    void ifDatabaseHasNoLectures_onFindAll_shouldReturnEmptyListOfLectures() {
        hibernateTemplate.deleteAll(expectedLectures);

        var lectures = lectureDao.findAll();

        assertThat(lectures).isEmpty();
    }

    @Test
    void givenLecture_onUpdate_shouldUpdateCorrectly() {
        lectureDao.update(lectureToUpdate);

        var expected = hibernateTemplate.get(Lecture.class, 2);

        assertEquals(lectureToUpdate, expected);
    }

    @Test
    void givenCorrectLectureId_onDelete_shouldDeleteCorrectly() {
        lectureDao.delete(expectedLecture2);

        var expected = hibernateTemplate.get(Lecture.class, 2);
        assertNull(expected);
    }

    @Test
    void givenClassroom_onFindByClassroom_shouldReturnCorrectListOfLectures() {
        List<Lecture> expected = new ArrayList<>(List.of(expectedLecture1));

        List<Lecture> actual = lectureDao.findByClassroom(expectedClassroom1);

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacher_onFindByTeacher_shouldReturnCorrectListOfLectures() {
        List<Lecture> expected = new ArrayList<>(List.of(expectedLecture1));

        List<Lecture> actual = lectureDao.findByTeacher(expectedTeacher1);

        assertEquals(expected, actual);
    }

    @Test
    void givenSubject_onFindBySubject_shouldReturnCorrectListOfLectures() {
        List<Lecture> expected = new ArrayList<>(List.of(expectedLecture1));

        List<Lecture> actual = lectureDao.findBySubject(expectedSubject1);

        assertEquals(expected, actual);
    }

    @Test
    void givenTimeslot_onFindByTimeslot_shouldReturnCorrectListOfLectures() {
        List<Lecture> expected = new ArrayList<>(List.of(expectedLecture1));

        List<Lecture> actual = lectureDao.findByTimeslot(expectedTimeslot1);

        assertEquals(expected, actual);
    }

    @Test
    void givenDateAndTimeslot_onFindByDateTime_shouldReturnCorrectListOfLectures() {
        List<Lecture> expected = new ArrayList<>(List.of(expectedLecture1));

        List<Lecture> actual = lectureDao.findByDateTime(LocalDate.of(2020, 1, 1), expectedTimeslot1);

        assertEquals(expected, actual);
    }

    @Test
    void givenDateTimeslotAndClassroom_onFindByDateTimeClassroom_shouldReturnOptionalWithCorrectLecture() {
        Optional<Lecture> expected = Optional.of(expectedLecture1);

        Optional<Lecture> actual = lectureDao.findByDateTimeClassroom(LocalDate.of(2020, 1, 1), expectedTimeslot1,
                expectedClassroom1);

        assertEquals(expected, actual);
    }

    @Test
    void givenWrongData_onFindByDateTimeClassroom_shouldReturnOptionalEmpty() {
        Optional<Lecture> actual = lectureDao.findByDateTimeClassroom(LocalDate.of(2025, 1, 1), expectedTimeslot1,
                expectedClassroom1);

        assertEquals(Optional.empty(), actual);
    }

    @Test
    void givenDateTimeslotAndTeacher_onFindByDateTimeTeacher_shouldReturnOptionalWithCorrectLecture() {
        Optional<Lecture> expected = Optional.of(expectedLecture1);

        Optional<Lecture> actual = lectureDao.findByDateTimeTeacher(LocalDate.of(2020, 1, 1), expectedTimeslot1,
                expectedTeacher1);

        assertEquals(expected, actual);
    }

    @Test
    void givenWrongData_onFindByDateTimeTeacher_shouldReturnOptionalEmpty() {
        Optional<Lecture> actual = lectureDao.findByDateTimeTeacher(LocalDate.of(2025, 1, 1), expectedTimeslot1,
                expectedTeacher1);

        assertEquals(Optional.empty(), actual);
    }

    @Test
    void givenTeacherAndDates_onFindByTeacherAndPeriod_shouldReturnCorrectListOfLectures() {
        var startDate = LocalDate.of(2020, 1, 1);
        var endDate = LocalDate.of(2020, 1, 3);
        List<Lecture> expected = new ArrayList<>(List.of(expectedLecture1));

        var actual = lectureDao.findByTeacherAndPeriod(expectedTeacher1, startDate, endDate);

        assertEquals(expected, actual);
    }

    @Test
    void givenStudentAndDates_onFindByStudentAndPeriod_shouldReturnCorrectListOfLectures() {
        var startDate = LocalDate.of(2020, 1, 1);
        var endDate = LocalDate.of(2020, 1, 3);

        var actual = lectureDao.findByStudentAndPeriod(expectedStudent1, startDate, endDate);

        assertEquals(expectedLectures, actual);
    }

    public interface TestData {
        List<Group> testGroups = new ArrayList<>(Arrays.asList(expectedGroup1, expectedGroup2));

        Lecture lectureToCreate = Lecture.builder().date(LocalDate.of(2010, 10, 10)).subject(expectedSubject1)
                .timeslot(expectedTimeslot1).groups(testGroups).teacher(expectedTeacher1)
                .classroom(expectedClassroom1).id(3).build();

        List<Group> expectedGroupsAfterUpdate = new ArrayList<>(List.of(expectedGroup2));

        Lecture lectureToUpdate = Lecture.builder().date(LocalDate.of(2010, 10, 10)).subject(expectedSubject1)
                .timeslot(expectedTimeslot1).groups(expectedGroupsAfterUpdate).teacher(expectedTeacher1)
                .classroom(expectedClassroom1).id(2).build();

        List<Group> expectedGroups1 = new ArrayList<>(Arrays.asList(expectedGroup1, expectedGroup2));
        List<Group> expectedGroups2 = new ArrayList<>(List.of(expectedGroup1));

        Lecture expectedLecture1 = Lecture.builder().date(LocalDate.of(2020, 1, 1)).subject(expectedSubject1)
                .id(1).timeslot(expectedTimeslot1).groups(expectedGroups1)
                .teacher(expectedTeacher1).classroom(expectedClassroom1).build();

        Lecture expectedLecture2 = Lecture.builder().date(LocalDate.of(2020, 1, 2)).subject(expectedSubject2)
                .id(2).timeslot(expectedTimeslot2).groups(expectedGroups2)
                .teacher(expectedTeacher2).classroom(expectedClassroom2).build();

        List<Lecture> expectedLectures = new ArrayList<>(Arrays.asList(expectedLecture1, expectedLecture2));
    }
}
