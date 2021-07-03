package ua.com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.sound.midi.Soundbank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;
import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import ua.com.foxminded.university.dao.jdbc.JdbcVacationDao;
import ua.com.foxminded.university.dao.jdbc.mappers.TeacherMapper;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Vacation;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(SpringTestConfig.class)
@Sql(scripts = { "classpath:schema.sql", "classpath:test-data.sql" })
class TeacherDaoTest {

    private static final String TEST_WHERE_CLAUSE = "first_name='Test' AND last_name='Teacher' AND gender='MALE' AND degree='DOCTOR' AND email='test@mail' AND phone='phone'";
    private static final String VACATIONS_WHERE_CLAUSE = "id=5 AND teacher_id=3 AND start_date='2020-01-01' AND end_date='2020-02-01'";
    private static final Subject TEST_SUBJECT = new Subject(2, "Test Subject", "For testing");
    private static final List<Subject> TEST_SUBJECTS = new ArrayList<Subject>(Arrays.asList(TEST_SUBJECT));
    private static final Address TEST_ADDRESS = new Address.Builder("test").id(7).postalCode("test").region("test")
	    .city("test").streetAddress("test").build();
    private static final Vacation TEST_VACATION = new Vacation(5, LocalDate.of(2020, 01, 01), LocalDate.of(2020, 02, 01));
    private static final List<Vacation> TEST_VACATIONS = new ArrayList<Vacation>(Arrays.asList(TEST_VACATION));

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AddressDao addressDao;
    @Mock
    private JdbcSubjectDao subjectDao;
    @Mock
    private JdbcVacationDao vacationDao;
    @InjectMocks
    @Autowired
    private TeacherMapper teacherMapper;
    @InjectMocks
    @Autowired
    private TeacherDao teacherDao;

    @Test
    void givenNewTeacher_onCreate_shouldCreateTeacherAndAssignSubjects() {
	Teacher teacher = new Teacher.Builder("Test", "Teacher").id(3)
		.gender(Gender.MALE).degree(Degree.DOCTOR).subjects(TEST_SUBJECTS)
		.email("test@mail").phoneNumber("phone").address(TEST_ADDRESS)
		.vacations(TEST_VACATIONS).build();

	System.out.println(teacher.getAddress().getId());

	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"teachers", "id = 3 AND address_id=7 AND " + TEST_WHERE_CLAUSE);
	int vacationsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"vacations", VACATIONS_WHERE_CLAUSE);

	teacherDao.create(teacher);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"teachers", "id = 3 AND address_id=7 AND " + TEST_WHERE_CLAUSE);
	int vacationsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"vacations", VACATIONS_WHERE_CLAUSE);

	assertEquals(vacationsAfterCreate, vacationsBeforeCreate + 1);
	assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

    @Test
    void givenCorrectTeacherId_onFindById_shouldReturnOptionalWithCorrectTeacher() {
	when(subjectDao.getSubjectsByTeacherId(2)).thenReturn(TEST_SUBJECTS);
	when(vacationDao.getVacationsByTeacherId(2)).thenReturn(TEST_VACATIONS);
	Address expectedAddress = new Address.Builder("Poland").id(2).postalCode("54321").region("Central region")
		.city("Warsaw").streetAddress("Urszuli Ledochowskiej 3").build();

	Teacher expectedTeacher = new Teacher.Builder("Marie", "Curie").id(2)
		.gender(Gender.FEMALE).degree(Degree.MASTER).subjects(TEST_SUBJECTS)
		.email("marie@curie.com").phoneNumber("+322223").address(expectedAddress)
		.vacations(TEST_VACATIONS).build();
	Optional<Teacher> expected = Optional.of(expectedTeacher);

	Optional<Teacher> actual = teacherDao.findById(2);

	verify(subjectDao).getSubjectsByTeacherId(2);
	verify(vacationDao).getVacationsByTeacherId(2);
	assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectTeacherId_onFindById_shouldReturnEmptyOptional() {
	Optional<Teacher> expected = Optional.empty();

	Optional<Teacher> actual = teacherDao.findById(5);

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasTeachers_onFindAll_shouldReturnCorrectListOfTeachers() {
	when(vacationDao.getVacationsByTeacherId(anyInt())).thenReturn(TEST_VACATIONS);
	when(subjectDao.getSubjectsByTeacherId(anyInt())).thenReturn(TEST_SUBJECTS);

	Address expectedAddress1 = new Address.Builder("UK").id(1).postalCode("12345").region("City-Of-Edinburgh")
		.city("Edinburgh").streetAddress("Panmure House").build();
	Teacher teacher1 = new Teacher.Builder("Adam", "Smith").id(1)
		.gender(Gender.MALE).degree(Degree.DOCTOR).subjects(TEST_SUBJECTS)
		.email("adam@smith.com").phoneNumber("+223322").address(expectedAddress1)
		.vacations(TEST_VACATIONS).build();
	Address expectedAddress2 = new Address.Builder("Poland").id(2).postalCode("54321").region("Central region")
		.city("Warsaw").streetAddress("Urszuli Ledochowskiej 3").build();
	Teacher teacher2 = new Teacher.Builder("Marie", "Curie").id(2)
		.gender(Gender.FEMALE).degree(Degree.MASTER).subjects(TEST_SUBJECTS)
		.email("marie@curie.com").phoneNumber("+322223").address(expectedAddress2)
		.vacations(TEST_VACATIONS).build();

	List<Teacher> expected = new ArrayList<>();
	expected.add(teacher1);
	expected.add(teacher2);

	List<Teacher> actual = teacherDao.findAll();

	verify(vacationDao, times(2)).getVacationsByTeacherId(anyInt());
	verify(subjectDao, times(2)).getSubjectsByTeacherId(anyInt());

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasNoTeachers_onFindAll_shouldReturnEmptyListOfTeachers() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "teachers");

	List<Teacher> teachers = teacherDao.findAll();

	assertThat(teachers).isEmpty();
    }

    @Test
    void givenTeacher_onUpdate_shouldUpdateCorrectly() {
	Address updatedAddress = new Address.Builder("test").id(2).postalCode("test").region("test")
		.city("test").streetAddress("test").build();
	Teacher teacher = new Teacher.Builder("Test", "Teacher").id(2)
		.gender(Gender.MALE).degree(Degree.DOCTOR).subjects(TEST_SUBJECTS)
		.email("test@mail").phoneNumber("phone").address(updatedAddress)
		.vacations(TEST_VACATIONS).build();

	int rowsBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"teachers", "id = 2 AND address_id=2 AND " + TEST_WHERE_CLAUSE);

	teacherDao.update(teacher);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"teachers", "id = 2 AND address_id=2 AND " + TEST_WHERE_CLAUSE);

	assertThat(rowsBeforeUpdate).isZero();
	assertThat(rowsAfterCreate).isEqualTo(1);
    }

    @Test
    void givenCorrectTeacherId_onDelete_shouldDeleteCorrectly() {
	int rowsBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", "id = 2");

	teacherDao.delete(2);

	int rowsAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", "id = 2");

	assertEquals(rowsAfterDelete, rowsBeforeDelete - 1);
    }
}
