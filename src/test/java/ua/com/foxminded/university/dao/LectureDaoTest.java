package ua.com.foxminded.university.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.expectedClassroom2;
import static ua.com.foxminded.university.dao.GroupDaoTest.TestData.expectedGroup1;
import static ua.com.foxminded.university.dao.GroupDaoTest.TestData.expectedGroup2;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLecture1;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLecture2;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLectures;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.lectureToCreate;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.lectureToUpdate;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.expectedSubject1;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.expectedSubject2;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.expectedTeacher1;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.expectedTeacher2;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.expectedTimeslot1;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.expectedTimeslot2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class LectureDaoTest {

    private static final String TEST_WHERE_CLAUSE = "date='2010-10-10' AND timeslot_id=1 AND subject_id=1 AND teacher_id=1 AND classroom_id=1";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private LectureDao lectureDao;

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

    @Test
    void givenNewLecture_onCreate_shouldCreateLectureAndAssignSubjects() {
	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"lectures", "id = 3 AND " + TEST_WHERE_CLAUSE);
	int lecturesGroupsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
		"lecture_id=3 AND group_id=1");
	lecturesGroupsBeforeCreate += JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
		"lecture_id=3 AND group_id=2");

	lectureDao.create(lectureToCreate);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"lectures", "id = 3 AND " + TEST_WHERE_CLAUSE);
	int lecturesGroupsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
		"lecture_id=3 AND group_id=1");
	lecturesGroupsAfterCreate += JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
		"lecture_id=3 AND group_id=2");

	assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
	assertThat(lecturesGroupsBeforeCreate).isZero();
	assertThat(lecturesGroupsAfterCreate).isEqualTo(2);
    }

    @Test
    void givenCorrectLectureId_onFindById_shouldReturnOptionalWithCorrectLecture() {
	Optional<Lecture> expected = Optional.of(expectedLecture2);

	Optional<Lecture> actual = lectureDao.findById(2);

	assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectLectureId_onFindById_shouldReturnEmptyOptional() {
	Optional<Lecture> expected = Optional.empty();

	Optional<Lecture> actual = lectureDao.findById(5);

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasLectures_onFindAll_shouldReturnCorrectListOfLectures() {
	List<Lecture> actual = lectureDao.findAll();

	assertEquals(expectedLectures, actual);
    }

    @Test
    void ifDatabaseHasNoLectures_onFindAll_shouldReturnEmptyListOfLectures() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "lectures");

	List<Lecture> lectures = lectureDao.findAll();

	assertThat(lectures).isEmpty();
    }

    @Test
    void givenLecture_onUpdate_shouldUpdateCorrectly() {
	int rowsBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"lectures", "id = 2 AND " + TEST_WHERE_CLAUSE);
	var group1AssignedBeforeUpdate = checkIfGroupIsAssignedToLecture(1, 2);
	var group2AssignedBeforeUpdate = checkIfGroupIsAssignedToLecture(2, 2);

	lectureDao.update(lectureToUpdate);

	int rowsAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"lectures", "id = 2 AND " + TEST_WHERE_CLAUSE);
	var group1AssignedAfterUpdate = checkIfGroupIsAssignedToLecture(1, 2);
	var group2AssignedAfterUpdate = checkIfGroupIsAssignedToLecture(2, 2);

	assertThat(rowsBeforeUpdate).isZero();
	assertThat(rowsAfterUpdate).isEqualTo(1);
	assertThat(group1AssignedBeforeUpdate).isTrue();
	assertThat(group2AssignedBeforeUpdate).isFalse();
	assertThat(group1AssignedAfterUpdate).isFalse();
	assertThat(group2AssignedAfterUpdate).isTrue();
    }

    @Test
    void givenCorrectLectureId_onDelete_shouldDeleteCorrectly() {
	int rowsBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures", "id = 2");

	lectureDao.delete(2);

	int rowsAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures", "id = 2");

	assertEquals(rowsAfterDelete, rowsBeforeDelete - 1);
    }

    boolean checkIfGroupIsAssignedToLecture(int groupId, int lectureId) {
	if (JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
		"lecture_id=" + lectureId + " AND group_id=" + groupId) == 1) {
	    return true;
	}
	return false;
    }

    public interface TestData {
	List<Group> testGroups = new ArrayList<Group>(Arrays.asList(expectedGroup1, expectedGroup2));

	Lecture lectureToCreate = Lecture.builder().date(LocalDate.of(2010, 10, 10)).subject(expectedSubject1)
		.timeslot(expectedTimeslot1).groups(testGroups).teacher(expectedTeacher1)
		.classroom(expectedClassroom1).id(3).build();

	List<Group> expectedGroupsAfterUpdate = new ArrayList<Group>(Arrays.asList(expectedGroup2));

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
