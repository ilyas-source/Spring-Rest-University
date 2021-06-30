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
import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;
import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.dao.jdbc.JdbcStudentDao;
import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import ua.com.foxminded.university.dao.jdbc.JdbcTimeslotDao;
import ua.com.foxminded.university.dao.jdbc.mappers.StudentMapper;
import ua.com.foxminded.university.menu.StudentsMenu;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Timeslot;

//INSERT INTO students (first_name, last_name, gender, birth_date, email, phone, address_id, group_id) VALUES
//('Ivan', 'Petrov', 'MALE', '1980-11-1', 'qwe@rty.com', '123123123', 3, 1),
//('John', 'Doe', 'MALE', '1981-11-1','qwe@qwe.com', '1231223', 4, 2),
//('Janna', 'DArk', 'FEMALE', '1881-11-1', 'qwe@no.fr', '1231223', 5, 1),
//('Mao', 'Zedun', 'MALE', '1921-9-14','qwe@no.cn', '1145223', 6, 2);

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(SpringTestConfig.class)
@Sql(scripts = { "classpath:schema.sql", "classpath:test-data.sql" })
class StudentDaoTest {

    private static final String TEST_WHERE_CLAUSE = "first_name = 'Name' AND last_name = 'Lastname' AND gender = 'MALE' " +
	    "AND birth_date = '1980-02-02' AND email = 'test@mail' AND phone = '+phone' " +
	    "AND address_id = 1 AND group_id = 1";
    private static final Address TEST_ADDRESS = new Address(1, "test", "test", "test", "test", "test");
    private static final Group TEST_GROUP = new Group(1, "test-01");

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StudentsMenu studentsMenu;
    @Mock
    private JdbcAddressDao addressDao;
    @Mock
    private JdbcGroupDao groupDao;
    @InjectMocks
    @Autowired
    private JdbcStudentDao studentDao;
    @InjectMocks
    @Autowired
    private StudentMapper studentMapper;

    @Test
    void givenNewStudent_onCreate_shouldCreateStudent() {
	Student student = new Student(5, "Name", "Lastname", null, LocalDate.of(1980, 2, 2), "test@mail", "+phone",
		TEST_ADDRESS, TEST_GROUP);

	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"students", "id = 5 AND " + TEST_WHERE_CLAUSE);

	studentDao.create(student);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"students", "id = 5");

	Student actual = studentDao.findById(5).get();
	System.out.println(studentsMenu.getStringFromStudent(actual));

	assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

//    @Test
//    void givenCorrectStudentId_onFindById_shouldReturnOptionalWithCorrectStudent() {
//
//	when(timeslotDao.findById(2)).thenReturn(Optional.of(TEST_TIMESLOT));
//	when(subjectDao.findById(2)).thenReturn(Optional.of(TEST_SUBJECT));
//	when(teacherDao.findById(2)).thenReturn(Optional.of(TEST_TEACHER));
//	when(classroomDao.findById(2)).thenReturn(Optional.of(TEST_CLASSROOM));
//	when(groupDao.findByStudentId(2)).thenReturn(TEST_GROUPS);
//
//	Student expectedStudent = new Student(2, LocalDate.of(2000, 1, 2), TEST_TIMESLOT, TEST_GROUPS, TEST_SUBJECT, TEST_TEACHER,
//		TEST_CLASSROOM);
//	Optional<Student> expected = Optional.of(expectedStudent);
//
//	Optional<Student> actual = studentDao.findById(2);
//
//	verify(timeslotDao).findById(2);
//	verify(subjectDao).findById(2);
//	verify(teacherDao).findById(2);
//	verify(classroomDao).findById(2);
//	verify(groupDao).findByStudentId(2);
//
//	assertEquals(expected, actual);
//    }
//
//    @Test
//    void givenIncorrectStudentId_onFindById_shouldReturnEmptyOptional() {
//	Optional<Student> expected = Optional.empty();
//
//	Optional<Student> actual = studentDao.findById(5);
//
//	assertEquals(expected, actual);
//    }
//
//    @Test
//    void ifDatabaseHasStudents_onFindAll_shouldReturnCorrectListOfStudents() {
//
//	when(timeslotDao.findById(anyInt())).thenReturn(Optional.of(TEST_TIMESLOT));
//	when(subjectDao.findById(anyInt())).thenReturn(Optional.of(TEST_SUBJECT));
//	when(teacherDao.findById(anyInt())).thenReturn(Optional.of(TEST_TEACHER));
//	when(classroomDao.findById(anyInt())).thenReturn(Optional.of(TEST_CLASSROOM));
//	when(groupDao.findByStudentId(anyInt())).thenReturn(TEST_GROUPS);
//
//	Student student1 = new Student(1, LocalDate.of(2000, 1, 1), TEST_TIMESLOT, TEST_GROUPS, TEST_SUBJECT, TEST_TEACHER,
//		TEST_CLASSROOM);
//	Student student2 = new Student(2, LocalDate.of(2000, 1, 2), TEST_TIMESLOT, TEST_GROUPS, TEST_SUBJECT, TEST_TEACHER,
//		TEST_CLASSROOM);
//
//	List<Student> expected = new ArrayList<>();
//	expected.add(student1);
//	expected.add(student2);
//
//	List<Student> actual = studentDao.findAll();
//
//	verify(timeslotDao, times(2)).findById(anyInt());
//	verify(subjectDao, times(2)).findById(anyInt());
//	verify(teacherDao, times(2)).findById(anyInt());
//	verify(classroomDao, times(2)).findById(anyInt());
//	verify(groupDao, times(2)).findByStudentId(anyInt());
//
//	assertEquals(expected, actual);
//    }
//
//    @Test
//    void ifDatabaseHasNoStudents_onFindAll_shouldReturnEmptyListOfStudents() {
//	JdbcTestUtils.deleteFromTables(jdbcTemplate, "students");
//
//	List<Student> students = studentDao.findAll();
//
//	assertThat(students).isEmpty();
//    }
//
//    @Test
//    void givenStudent_onUpdate_shouldUpdateCorrectly() {
//	List<Group> testGroupsBeforeUpdate = new ArrayList<Group>(Arrays.asList(new Group(1, "Test-01")));
//	List<Group> testGroupsAfterUpdate = new ArrayList<Group>(Arrays.asList(new Group(2, "Test-02")));
//
//	Student student = new Student(2, LocalDate.of(2010, 10, 10), TEST_TIMESLOT, testGroupsAfterUpdate,
//		TEST_SUBJECT, TEST_TEACHER, TEST_CLASSROOM);
//
//	int rowsBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//		"students", "id = 2 AND " + TEST_WHERE_CLAUSE);
//
//	int group1AssignedBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students_groups",
//		"student_id=2 AND group_id=1");
//	int group2AssignedBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students_groups",
//		"student_id=2 AND group_id=2");
//
//	when(groupDao.findByStudentId(2)).thenReturn(testGroupsBeforeUpdate);
//
//	studentDao.update(student);
//
//	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//		"students", "id = 2 AND " + TEST_WHERE_CLAUSE);
//
//	int group1AssignedAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students_groups",
//		"student_id=2 AND group_id=1");
//
//	int group2AssignedAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students_groups",
//		"student_id=2 AND group_id=2");
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
//    void givenCorrectStudentId_onDelete_shouldDeleteCorrectly() {
//	int rowsBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students", "id = 2");
//
//	studentDao.delete(2);
//
//	int rowsAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students", "id = 2");
//
//	assertEquals(rowsAfterDelete, rowsBeforeDelete - 1);
//    }
}
