package ua.com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.dao.jdbc.JdbcLectureDao;
import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import ua.com.foxminded.university.dao.jdbc.JdbcTimeslotDao;
import ua.com.foxminded.university.dao.jdbc.JdbcVacationDao;
import ua.com.foxminded.university.dao.jdbc.mappers.LectureMapper;
import ua.com.foxminded.university.menu.TeachersMenu;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Timeslot;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(SpringTestConfig.class)
@Sql(scripts = { "classpath:schema.sql", "classpath:test-data.sql" })
class LectureDaoTest {

    private static final String TEST_WHERE_CLAUSE = "date='2010-10-10' AND timeslot_id=1 AND subject_id=2 AND teacher_id=1 AND classroom_id=1";
    private static final Timeslot TEST_TIMESLOT = new Timeslot(1, LocalTime.of(9, 0), LocalTime.of(10, 0));
    private static final Subject TEST_SUBJECT = new Subject(2, "Test Subject", "For testing");
    private static final Address TEST_ADDRESS = new Address.Builder("France").id(1).postalCode("21012")
	    .region("Central").city("Paris").streetAddress("Rue 15").build();
    private static final Teacher TEST_TEACHER = new Teacher.Builder("Adam", "Smith")
	    .id(1).gender(Gender.MALE).degree(Degree.DOCTOR)
	    .subjects(new ArrayList<Subject>(Arrays.asList(TEST_SUBJECT)))
	    .email("adam@smith.com").phoneNumber("+223322")
	    .address(TEST_ADDRESS).build();

    private static final Classroom TEST_CLASSROOM = new Classroom(1, new Location(1, "Test building", 1, 5), "Test classroom",
	    15);
    private static final List<Group> TEST_GROUPS = new ArrayList<Group>(
	    Arrays.asList(new Group(1, "Test-01"), new Group(2, "Test-02")));
    private static final LocalDate TEST_DATE = LocalDate.of(2010, 10, 10);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Mock
    private JdbcTimeslotDao timeslotDao;
    @Mock
    private JdbcSubjectDao subjectDao;
    @Mock
    private TeacherDao teacherDao;
    @Mock
    private JdbcClassroomDao classroomDao;
    @Mock
    private JdbcGroupDao groupDao;
    @Mock
    private VacationDao vacationDao;
    @InjectMocks
    @Autowired
    private LectureDao lectureDao;
    @InjectMocks
    @Autowired
    private LectureMapper lectureMapper;

    @Test
    void givenNewLecture_onCreate_shouldCreateLectureAndAssignSubjects() {
	Lecture lecture = new Lecture.Builder(TEST_DATE, TEST_SUBJECT).timeslot(TEST_TIMESLOT)
		.groups(TEST_GROUPS).teacher(TEST_TEACHER).classroom(TEST_CLASSROOM)
		.build();

	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"lectures", "id = 3 AND " + TEST_WHERE_CLAUSE);
	int lecturesGroupsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
		"lecture_id=3 AND group_id=1");
	lecturesGroupsBeforeCreate += JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
		"lecture_id=3 AND group_id=2");

	lectureDao.create(lecture);

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
	when(timeslotDao.findById(2)).thenReturn(Optional.of(TEST_TIMESLOT));
	when(subjectDao.findById(2)).thenReturn(Optional.of(TEST_SUBJECT));
	when(teacherDao.findById(2)).thenReturn(Optional.of(TEST_TEACHER));
	when(classroomDao.findById(2)).thenReturn(Optional.of(TEST_CLASSROOM));
	when(groupDao.findByLectureId(2)).thenReturn(TEST_GROUPS);

	Lecture expectedLecture = new Lecture.Builder(LocalDate.of(2000, 1, 2), TEST_SUBJECT)
		.id(2)
		.timeslot(TEST_TIMESLOT)
		.groups(TEST_GROUPS)
		.teacher(TEST_TEACHER)
		.classroom(TEST_CLASSROOM)
		.build();
	Optional<Lecture> expected = Optional.of(expectedLecture);

	Optional<Lecture> actual = lectureDao.findById(2);

	verify(timeslotDao).findById(2);
	verify(subjectDao).findById(2);
	verify(teacherDao).findById(2);
	verify(classroomDao).findById(2);
	verify(groupDao).findByLectureId(2);

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
	when(timeslotDao.findById(anyInt())).thenReturn(Optional.of(TEST_TIMESLOT));
	when(subjectDao.findById(anyInt())).thenReturn(Optional.of(TEST_SUBJECT));
	when(teacherDao.findById(anyInt())).thenReturn(Optional.of(TEST_TEACHER));
	when(classroomDao.findById(anyInt())).thenReturn(Optional.of(TEST_CLASSROOM));
	when(groupDao.findByLectureId(anyInt())).thenReturn(TEST_GROUPS);

	Lecture lecture1 = new Lecture.Builder(LocalDate.of(2000, 1, 1), TEST_SUBJECT)
		.id(1).timeslot(TEST_TIMESLOT).groups(TEST_GROUPS)
		.teacher(TEST_TEACHER).classroom(TEST_CLASSROOM).build();

	Lecture lecture2 = new Lecture.Builder(LocalDate.of(2000, 1, 2), TEST_SUBJECT)
		.id(2).timeslot(TEST_TIMESLOT).groups(TEST_GROUPS)
		.teacher(TEST_TEACHER).classroom(TEST_CLASSROOM).build();

	List<Lecture> expected = new ArrayList<>();
	expected.add(lecture1);
	expected.add(lecture2);

	List<Lecture> actual = lectureDao.findAll();

	verify(timeslotDao, times(2)).findById(anyInt());
	verify(subjectDao, times(2)).findById(anyInt());
	verify(teacherDao, times(2)).findById(anyInt());
	verify(classroomDao, times(2)).findById(anyInt());
	verify(groupDao, times(2)).findByLectureId(anyInt());

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasNoLectures_onFindAll_shouldReturnEmptyListOfLectures() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "lectures");

	List<Lecture> lectures = lectureDao.findAll();

	assertThat(lectures).isEmpty();
    }

    @Test
    void givenLecture_onUpdate_shouldUpdateCorrectly() {
	List<Group> testGroupsBeforeUpdate = new ArrayList<Group>(Arrays.asList(new Group(1, "Test-01")));
	List<Group> testGroupsAfterUpdate = new ArrayList<Group>(Arrays.asList(new Group(2, "Test-02")));

	Lecture lecture = new Lecture.Builder(LocalDate.of(2010, 10, 10), TEST_SUBJECT)
		.id(2)
		.groups(testGroupsAfterUpdate)
		.timeslot(TEST_TIMESLOT)
		.teacher(TEST_TEACHER)
		.classroom(TEST_CLASSROOM)
		.build();

	int rowsBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"lectures", "id = 2 AND " + TEST_WHERE_CLAUSE);

	boolean group1AssignedBeforeUpdate = checkIfGroupIsAssignedToLecture(1, 2);
	boolean group2AssignedBeforeUpdate = checkIfGroupIsAssignedToLecture(2, 2);

	when(groupDao.findByLectureId(2)).thenReturn(testGroupsBeforeUpdate);

	lectureDao.update(lecture);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"lectures", "id = 2 AND " + TEST_WHERE_CLAUSE);

	boolean group1AssignedAfterUpdate = checkIfGroupIsAssignedToLecture(1, 2);
	boolean group2AssignedAfterUpdate = checkIfGroupIsAssignedToLecture(2, 2);

	assertThat(rowsBeforeUpdate).isZero();
	assertThat(rowsAfterCreate).isEqualTo(1);

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
}
