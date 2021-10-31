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
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.expectedClassroom2;
import static ua.com.foxminded.university.dao.GroupDaoTest.TestData.expectedGroup1;
import static ua.com.foxminded.university.dao.GroupDaoTest.TestData.expectedGroup2;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.StudentDaoTest.TestData.expectedStudent1;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.expectedSubject1;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.expectedSubject2;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.expectedTeacher1;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.expectedTeacher2;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.expectedTimeslot1;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.expectedTimeslot2;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class LectureDaoTest {

    private static final String TEST_WHERE_CLAUSE = "date='2010-10-10' AND timeslot_id=1 AND subject_id=1 AND teacher_id=1 AND classroom_id=1";

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
        List<Lecture> expected = new ArrayList<>(Arrays.asList(expectedLecture1));

        List<Lecture> actual = lectureDao.findByClassroom(expectedClassroom1);

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacher_onFindByTeacher_shouldReturnCorrectListOfLectures() {
        List<Lecture> expected = new ArrayList<>(Arrays.asList(expectedLecture1));

        List<Lecture> actual = lectureDao.findByTeacher(expectedTeacher1);

        assertEquals(expected, actual);
    }

    @Test
    void givenSubject_onFindBySubject_shouldReturnCorrectListOfLectures() {
        List<Lecture> expected = new ArrayList<>(Arrays.asList(expectedLecture1));

        List<Lecture> actual = lectureDao.findBySubject(expectedSubject1);

        assertEquals(expected, actual);
    }

    @Test
    void givenTimeslot_onFindByTimeslot_shouldReturnCorrectListOfLectures() {
        List<Lecture> expected = new ArrayList<>(Arrays.asList(expectedLecture1));

        List<Lecture> actual = lectureDao.findByTimeslot(expectedTimeslot1);

        assertEquals(expected, actual);
    }

    @Test
    void givenDateAndTimeslot_onFindByDateTime_shouldReturnCorrectListOfLectures() {
        List<Lecture> expected = new ArrayList<>(Arrays.asList(expectedLecture1));

        List<Lecture> actual = lectureDao.findByDateTime(LocalDate.of(2020, 1, 1), expectedTimeslot1);

        assertEquals(expected, actual);
    }

    @Test
    void givenDateTimeslotAndClassroom_onFindByDateTimeClassroom_shouldReturnOptionalwithCorrectLecture() {
        Optional<Lecture> expected = Optional.of(expectedLecture1);

        Optional<Lecture> actual = lectureDao.findByDateTimeClassroom(LocalDate.of(2020, 1, 1), expectedTimeslot1,
                expectedClassroom1);

        assertEquals(expected, actual);
    }

    @Test
    void givenDateTimeslotAndTeacher_onFindByDateTimeTeacher_shouldReturnOptionalwithCorrectLecture() {
        Optional<Lecture> expected = Optional.of(expectedLecture1);

        Optional<Lecture> actual = lectureDao.findByDateTimeTeacher(LocalDate.of(2020, 1, 1), expectedTimeslot1,
                expectedTeacher1);

        assertEquals(expected, actual);
    }

//    @Test
//    void givenNewLecture_onCreate_shouldCreateLectureAndAssignSubjects() {
//        int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//                "lectures", "id = 3 AND " + TEST_WHERE_CLAUSE);
//        int lecturesGroupsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
//                "lecture_id=3 AND group_id=1");
//        lecturesGroupsBeforeCreate += JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
//                "lecture_id=3 AND group_id=2");
//
//        lectureDao.create(lectureToCreate);
//
//        int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//                "lectures", "id = 3 AND " + TEST_WHERE_CLAUSE);
//        int lecturesGroupsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
//                "lecture_id=3 AND group_id=1");
//        lecturesGroupsAfterCreate += JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
//                "lecture_id=3 AND group_id=2");
//
//        assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
//        assertThat(lecturesGroupsBeforeCreate).isZero();
//        assertThat(lecturesGroupsAfterCreate).isEqualTo(2);
//    }

//    @Test
//    void givenLecture_onUpdate_shouldUpdateCorrectly() {
//        int rowsBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//                "lectures", "id = 2 AND " + TEST_WHERE_CLAUSE);
//        var group1AssignedBeforeUpdate = checkIfGroupIsAssignedToLecture(1, 2);
//        var group2AssignedBeforeUpdate = checkIfGroupIsAssignedToLecture(2, 2);
//
//        lectureDao.update(lectureToUpdate);
//
//        int rowsAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//                "lectures", "id = 2 AND " + TEST_WHERE_CLAUSE);
//        var group1AssignedAfterUpdate = checkIfGroupIsAssignedToLecture(1, 2);
//        var group2AssignedAfterUpdate = checkIfGroupIsAssignedToLecture(2, 2);
//
//        assertThat(rowsBeforeUpdate).isZero();
//        assertThat(rowsAfterUpdate).isEqualTo(1);
//        assertThat(group1AssignedBeforeUpdate).isTrue();
//        assertThat(group2AssignedBeforeUpdate).isFalse();
//        assertThat(group1AssignedAfterUpdate).isFalse();
//        assertThat(group2AssignedAfterUpdate).isTrue();
//    }

    @Test
    void givenTeacherAndDates_onFindByTeacherAndPeriod_shouldReturnCorrectListOfLectures() {
        var startDate=LocalDate.of(2020,1,1);
        var endDate=LocalDate.of(2020,1,3);
        List<Lecture> expected=new ArrayList<Lecture>(Arrays.asList(expectedLecture1));

        var actual = lectureDao.findByTeacherAndPeriod(expectedTeacher1, startDate, endDate);

        assertEquals(expected, actual);
    }

    @Test
    void givenStudentAndDates_onFindByStudentAndPeriod_shouldReturnCorrectListOfLectures() {
        var startDate=LocalDate.of(2020,1,1);
        var endDate=LocalDate.of(2020,1,3);

        var actual = lectureDao.findByStudentAndPeriod(expectedStudent1, startDate, endDate);

        assertEquals(expectedLectures, actual);
    }

//    boolean checkIfGroupIsAssignedToLecture(int groupId, int lectureId) {
//        if (JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
//                "lecture_id=" + lectureId + " AND group_id=" + groupId) == 1) {
//            return true;
//        }
//        return false;
//    }

    public interface TestData {
        List<Group> testGroups = new ArrayList<>(Arrays.asList(expectedGroup1, expectedGroup2));

        Lecture lectureToCreate = Lecture.builder().date(LocalDate.of(2010, 10, 10)).subject(expectedSubject1)
                .timeslot(expectedTimeslot1).groups(testGroups).teacher(expectedTeacher1)
                .classroom(expectedClassroom1).id(3).build();

        List<Group> expectedGroupsAfterUpdate = new ArrayList<>(Arrays.asList(expectedGroup2));

        Lecture lectureToUpdate = Lecture.builder().date(LocalDate.of(2010, 10, 10)).subject(expectedSubject1)
                .timeslot(expectedTimeslot1).groups(expectedGroupsAfterUpdate).teacher(expectedTeacher1)
                .classroom(expectedClassroom1).id(2).build();

        List<Group> expectedGroups1 = new ArrayList<>(Arrays.asList(expectedGroup1, expectedGroup2));
        List<Group> expectedGroups2 = new ArrayList<>(Arrays.asList(expectedGroup1));

        Lecture expectedLecture1 = Lecture.builder().date(LocalDate.of(2020, 1, 1)).subject(expectedSubject1)
                .id(1).timeslot(expectedTimeslot1).groups(expectedGroups1)
                .teacher(expectedTeacher1).classroom(expectedClassroom1).build();

        Lecture expectedLecture2 = Lecture.builder().date(LocalDate.of(2020, 1, 2)).subject(expectedSubject2)
                .id(2).timeslot(expectedTimeslot2).groups(expectedGroups2)
                .teacher(expectedTeacher2).classroom(expectedClassroom2).build();

        List<Lecture> expectedLectures = new ArrayList<>(Arrays.asList(expectedLecture1, expectedLecture2));
    }
}
