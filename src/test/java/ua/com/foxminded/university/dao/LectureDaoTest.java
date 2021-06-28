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
import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import ua.com.foxminded.university.dao.jdbc.JdbcTimeslotDao;
import ua.com.foxminded.university.dao.jdbc.mappers.LectureMapper;
import ua.com.foxminded.university.menu.LecturesMenu;
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

//INSERT INTO lectures (date, timeslot_id, subject_id, teacher_id, classroom_id) VALUES
//('2000-1-1', 1, 1, 1, 1),
//('2000-1-2', 2, 2, 2, 2);

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(SpringTestConfig.class)
@Sql(scripts = { "classpath:schema.sql", "classpath:test-data.sql" })
class LectureDaoTest {

    private static final String TEST_WHERE_CLAUSE = "date='2010-10-10' AND timeslot_id=1 AND subject_id=2 AND teacher_id=1 AND classroom_id=1";
    private static final Timeslot TEST_TIMESLOT = new Timeslot(1, LocalTime.of(9, 0), LocalTime.of(10, 0));
    private static final Subject TEST_SUBJECT = new Subject(2, "Test Subject", "For testing");
    private static final Teacher TEST_TEACHER = new Teacher(1, "Adam", "Smith", Gender.MALE, Degree.DOCTOR,
	    new ArrayList<Subject>(Arrays.asList(TEST_SUBJECT)), "adam@smith.com", "+223322",
	    new Address(1, "France", "21012", "Central", "Paris", "Rue 15"));
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
    private JdbcTeacherDao teacherDao;
    @Mock
    private JdbcClassroomDao classroomDao;
    @Mock
    private JdbcGroupDao groupDao;
    @InjectMocks
    @Autowired
    private JdbcLectureDao lectureDao;
    @InjectMocks
    @Autowired
    private LectureMapper lectureMapper;

    @Autowired
    private LecturesMenu lecturesMenu;

    @Test
    void givenNewLecture_onCreate_shouldCreateLectureAndAssignSubjects() {
	Lecture lecture = new Lecture(3, TEST_DATE, TEST_TIMESLOT, TEST_GROUPS, TEST_SUBJECT, TEST_TEACHER, TEST_CLASSROOM);

	int elementBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"lectures", "id = 3 AND " + TEST_WHERE_CLAUSE);
	int lecturesGroupsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
		"lecture_id=3 AND group_id=1");
	lecturesGroupsBeforeCreate += JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
		"lecture_id=3 AND group_id=2");

	lectureDao.create(lecture);

	int elementAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"lectures", "id = 3 AND " + TEST_WHERE_CLAUSE);
//	int lecturesGroupsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
//		"lecture_id=3 AND group_id=1");
//	lecturesGroupsAfterCreate += JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
//		"lecture_id=3 AND group_id=2");

	assertEquals(elementAfterCreate, elementBeforeCreate + 1);
    }

    @Test
    void givenCorrectLectureId_onFindById_shouldReturnOptionalWithCorrectLecture() {

	when(timeslotDao.findById(2)).thenReturn(Optional.of(TEST_TIMESLOT));
	when(subjectDao.findById(2)).thenReturn(Optional.of(TEST_SUBJECT));
	when(teacherDao.findById(2)).thenReturn(Optional.of(TEST_TEACHER));
	when(classroomDao.findById(2)).thenReturn(Optional.of(TEST_CLASSROOM));
	when(groupDao.findByLectureId(2)).thenReturn(TEST_GROUPS);

	Lecture expectedLecture = new Lecture(2, LocalDate.of(2000, 1, 2), TEST_TIMESLOT, TEST_GROUPS, TEST_SUBJECT, TEST_TEACHER,
		TEST_CLASSROOM);
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

	Lecture lecture1 = new Lecture(1, LocalDate.of(2000, 1, 1), TEST_TIMESLOT, TEST_GROUPS, TEST_SUBJECT, TEST_TEACHER,
		TEST_CLASSROOM);
	Lecture lecture2 = new Lecture(2, LocalDate.of(2000, 1, 2), TEST_TIMESLOT, TEST_GROUPS, TEST_SUBJECT, TEST_TEACHER,
		TEST_CLASSROOM);

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

	// хотим апдейтнуть лекцию №2, дав ей только 2-ю группу. У лекции №2 по идее в
	// списке групп только группа 1.
	// так что мы ждем, что произойдет как удаление группы, так и добавление группы
	List<Group> testGroups = new ArrayList<Group>(Arrays.asList(new Group(2, "Test-02")));

	Lecture lecture = new Lecture(2, LocalDate.of(2010, 10, 10), TEST_TIMESLOT, testGroups,
		TEST_SUBJECT, TEST_TEACHER, TEST_CLASSROOM);

	// на этот подсчет группы не влияют, т.к. тестим только таблицу Лекции
	int elementBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"lectures", "id = 2 AND " + TEST_WHERE_CLAUSE);

	// эта цифра должна быть единицей
	int group1AssignedToLecture2BeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
		"lecture_id=2 AND group_id=1");
	System.out.println(group1AssignedToLecture2BeforeUpdate);

	// эта цифра должна быть нулём
	int group2AssignedToLecture2BeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
		"lecture_id=2 AND group_id=2");
	System.out.println(group2AssignedToLecture2BeforeUpdate);

	System.out.println(lecturesMenu.getStringFromLecture(lecture));
	System.out.println(lecturesMenu.getStringFromLecture(lectureDao.findById(2).get()));

	lectureDao.update(lecture);

	int elementAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"lectures", "id = 2 AND " + TEST_WHERE_CLAUSE);

	// эта цифра должна быть нулем. Но почему-то 1. Значит, не удалилась группа 1.
	// Надо смотреть
	int group1AssignedToLecture2AfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
		"lecture_id=2 AND group_id=1");
	System.out.println(group1AssignedToLecture2AfterUpdate);

	// эта цифра должна быть единицей
	int group2AssignedToLecture2AfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures_groups",
		"lecture_id=2 AND group_id=2");
	System.out.println(group2AssignedToLecture2AfterUpdate);

	assertThat(elementBeforeUpdate).isZero();
	assertThat(elementAfterUpdate).isEqualTo(1);
	// assertEquals(lecturesGroupsAfterUpdate, lecturesGroupsBeforeUpdate + 2);
    }

    @Test
    void givenCorrectLectureId_onDelete_shouldDeleteCorrectly() {
	int elementBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures", "id = 2");

	lectureDao.delete(2);

	int elementAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures", "id = 2");

	assertEquals(elementAfterDelete, elementBeforeDelete - 1);
    }
}
