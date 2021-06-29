//package ua.com.foxminded.university.dao;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import org.checkerframework.common.reflection.qual.NewInstance;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//import org.springframework.test.jdbc.JdbcTestUtils;
//
//import ua.com.foxminded.university.SpringTestConfig;
//import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;
//import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
//import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
//import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDao;
//import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDao;
//import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDao;
//import ua.com.foxminded.university.dao.jdbc.JdbcTimeslotDao;
//import ua.com.foxminded.university.dao.jdbc.JdbcVacationDao;
//import ua.com.foxminded.university.dao.jdbc.mappers.TeacherMapper;
//import ua.com.foxminded.university.menu.TeachersMenu;
//import ua.com.foxminded.university.model.Address;
//import ua.com.foxminded.university.model.Classroom;
//import ua.com.foxminded.university.model.Degree;
//import ua.com.foxminded.university.model.Gender;
//import ua.com.foxminded.university.model.Group;
//import ua.com.foxminded.university.model.Teacher;
//import ua.com.foxminded.university.model.Location;
//import ua.com.foxminded.university.model.Subject;
//import ua.com.foxminded.university.model.Teacher;
//import ua.com.foxminded.university.model.Timeslot;
//import ua.com.foxminded.university.model.Vacation;
//
//@ExtendWith(MockitoExtension.class)
//@SpringJUnitConfig(SpringTestConfig.class)
//@Sql(scripts = { "classpath:schema.sql", "classpath:test-data.sql" })
//class TeacherDaoTest {
//
//    // TODO private static final String TEST_WHERE_CLAUSE = "date='2010-10-10' AND
//    // timeslot_id=1 AND subject_id=2 AND teacher_id=1 AND classroom_id=1";
//
//    private static final Subject TEST_SUBJECT = new Subject(2, "Test Subject", "For testing");
//    private static final List<Subject> TEST_SUBJECTS = new ArrayList<Subject>(Arrays.asList(TEST_SUBJECT));
//
//    private static final Vacation TEST_VACATION = new Vacation(0, null, null, null)
//    
//    private static final List<Vacation>TEST_VACATIONS = new ArrayList<Vacation>(Arrays.asList(New Vacation()));
//
//    @Mock
//    private JdbcSubjectDao subjectDao;
//    @Mock
//    private JdbcAddressDao addressDao;
//    @Mock
//    private JdbcVacationDao vacationDao;
//    @InjectMocks
//    @Autowired
//    private JdbcTeacherDao teacherDao;
//    @InjectMocks
//    @Autowired
//    private TeacherMapper teacherMapper;
//
//    @Test
//    void givenNewTeacher_onCreate_shouldCreateTeacherAndAssignSubjects() {
//	Teacher teacher = new Teacher(3, TEST_DATE, TEST_TIMESLOT, TEST_GROUPS, TEST_SUBJECT, TEST_TEACHER, TEST_CLASSROOM);
//
//	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//		"teachers", "id = 3 AND " + TEST_WHERE_CLAUSE);
//	int teachersGroupsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers_groups",
//		"teacher_id=3 AND group_id=1");
//	teachersGroupsBeforeCreate += JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers_groups",
//		"teacher_id=3 AND group_id=2");
//
//	teacherDao.create(teacher);
//
//	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//		"teachers", "id = 3 AND " + TEST_WHERE_CLAUSE);
//	int teachersGroupsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers_groups",
//		"teacher_id=3 AND group_id=1");
//	teachersGroupsAfterCreate += JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers_groups",
//		"teacher_id=3 AND group_id=2");
//
//	assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
//	assertThat(teachersGroupsBeforeCreate).isZero();
//	assertThat(teachersGroupsAfterCreate).isEqualTo(2);
//    }
//
//    @Test
//    void givenCorrectTeacherId_onFindById_shouldReturnOptionalWithCorrectTeacher() {
//
//	when(timeslotDao.findById(2)).thenReturn(Optional.of(TEST_TIMESLOT));
//	when(subjectDao.findById(2)).thenReturn(Optional.of(TEST_SUBJECT));
//	when(teacherDao.findById(2)).thenReturn(Optional.of(TEST_TEACHER));
//	when(classroomDao.findById(2)).thenReturn(Optional.of(TEST_CLASSROOM));
//	when(groupDao.findByTeacherId(2)).thenReturn(TEST_GROUPS);
//
//	Teacher expectedTeacher = new Teacher(2, LocalDate.of(2000, 1, 2), TEST_TIMESLOT, TEST_GROUPS, TEST_SUBJECT, TEST_TEACHER,
//		TEST_CLASSROOM);
//	Optional<Teacher> expected = Optional.of(expectedTeacher);
//
//	Optional<Teacher> actual = teacherDao.findById(2);
//
//	verify(timeslotDao).findById(2);
//	verify(subjectDao).findById(2);
//	verify(teacherDao).findById(2);
//	verify(classroomDao).findById(2);
//	verify(groupDao).findByTeacherId(2);
//
//	assertEquals(expected, actual);
//    }
//
//    @Test
//    void givenIncorrectTeacherId_onFindById_shouldReturnEmptyOptional() {
//	Optional<Teacher> expected = Optional.empty();
//
//	Optional<Teacher> actual = teacherDao.findById(5);
//
//	assertEquals(expected, actual);
//    }
//
//    @Test
//    void ifDatabaseHasTeachers_onFindAll_shouldReturnCorrectListOfTeachers() {
//
//	when(timeslotDao.findById(anyInt())).thenReturn(Optional.of(TEST_TIMESLOT));
//	when(subjectDao.findById(anyInt())).thenReturn(Optional.of(TEST_SUBJECT));
//	when(teacherDao.findById(anyInt())).thenReturn(Optional.of(TEST_TEACHER));
//	when(classroomDao.findById(anyInt())).thenReturn(Optional.of(TEST_CLASSROOM));
//	when(groupDao.findByTeacherId(anyInt())).thenReturn(TEST_GROUPS);
//
//	Teacher teacher1 = new Teacher(1, LocalDate.of(2000, 1, 1), TEST_TIMESLOT, TEST_GROUPS, TEST_SUBJECT, TEST_TEACHER,
//		TEST_CLASSROOM);
//	Teacher teacher2 = new Teacher(2, LocalDate.of(2000, 1, 2), TEST_TIMESLOT, TEST_GROUPS, TEST_SUBJECT, TEST_TEACHER,
//		TEST_CLASSROOM);
//
//	List<Teacher> expected = new ArrayList<>();
//	expected.add(teacher1);
//	expected.add(teacher2);
//
//	List<Teacher> actual = teacherDao.findAll();
//
//	verify(timeslotDao, times(2)).findById(anyInt());
//	verify(subjectDao, times(2)).findById(anyInt());
//	verify(teacherDao, times(2)).findById(anyInt());
//	verify(classroomDao, times(2)).findById(anyInt());
//	verify(groupDao, times(2)).findByTeacherId(anyInt());
//
//	assertEquals(expected, actual);
//    }
//
//    @Test
//    void ifDatabaseHasNoTeachers_onFindAll_shouldReturnEmptyListOfTeachers() {
//	JdbcTestUtils.deleteFromTables(jdbcTemplate, "teachers");
//
//	List<Teacher> teachers = teacherDao.findAll();
//
//	assertThat(teachers).isEmpty();
//    }
//
//    @Test
//    void givenTeacher_onUpdate_shouldUpdateCorrectly() {
//	List<Group> testGroupsBeforeUpdate = new ArrayList<Group>(Arrays.asList(new Group(1, "Test-01")));
//	List<Group> testGroupsAfterUpdate = new ArrayList<Group>(Arrays.asList(new Group(2, "Test-02")));
//
//	Teacher teacher = new Teacher(2, LocalDate.of(2010, 10, 10), TEST_TIMESLOT, testGroupsAfterUpdate,
//		TEST_SUBJECT, TEST_TEACHER, TEST_CLASSROOM);
//
//	int rowsBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//		"teachers", "id = 2 AND " + TEST_WHERE_CLAUSE);
//
//	int group1AssignedBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers_groups",
//		"teacher_id=2 AND group_id=1");
//	int group2AssignedBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers_groups",
//		"teacher_id=2 AND group_id=2");
//
//	when(groupDao.findByTeacherId(2)).thenReturn(testGroupsBeforeUpdate);
//
//	teacherDao.update(teacher);
//
//	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//		"teachers", "id = 2 AND " + TEST_WHERE_CLAUSE);
//
//	int group1AssignedAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers_groups",
//		"teacher_id=2 AND group_id=1");
//
//	int group2AssignedAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers_groups",
//		"teacher_id=2 AND group_id=2");
//
//	assertThat(rowsBeforeUpdate).isZero();
//	assertThat(rowsAfterCreate).isEqualTo(1);
//
//	assertThat(group1AssignedBeforeUpdate).isEqualTo(1);
//	assertThat(group2AssignedBeforeUpdate).isZero();
//
//	assertThat(group1AssignedAfterUpdate).isZero();
//	assertThat(group2AssignedAfterUpdate).isEqualTo(1);
//    }
//
//    @Test
//    void givenCorrectTeacherId_onDelete_shouldDeleteCorrectly() {
//	int rowsBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", "id = 2");
//
//	teacherDao.delete(2);
//
//	int rowsAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", "id = 2");
//
//	assertEquals(rowsAfterDelete, rowsBeforeDelete - 1);
//    }
//}
