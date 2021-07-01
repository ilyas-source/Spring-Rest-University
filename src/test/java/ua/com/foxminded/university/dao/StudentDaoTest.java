package ua.com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
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
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.dao.jdbc.JdbcStudentDao;
import ua.com.foxminded.university.dao.jdbc.mappers.StudentMapper;
import ua.com.foxminded.university.menu.StudentsMenu;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

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
	    "AND address_id = 4 AND group_id = 2";
    private static final Address TEST_ADDRESS = new Address(4, "test", "test", "test", "test", "test");
    private static final Group TEST_GROUP = new Group(2, "test-01");

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
	Student student = new Student.Builder("Name", "Lastname")
		.id(5).gender(Gender.MALE)
		.birthDate(LocalDate.of(1980, 2, 2))
		.email("test@mail").phone("+phone")
		.address(TEST_ADDRESS).group(TEST_GROUP)
		.build();
	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"students", "id = 5 AND " + TEST_WHERE_CLAUSE);

	studentDao.create(student);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"students", "id = 5 AND " + TEST_WHERE_CLAUSE);

	assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

    @Test
    void givenCorrectStudentId_onFindById_shouldReturnOptionalWithCorrectStudent() {

	when(groupDao.findById(2)).thenReturn(Optional.of(TEST_GROUP));
	when(addressDao.findById(4)).thenReturn(Optional.of(TEST_ADDRESS));

	Student expectedStudent = new Student.Builder("John", "Doe")
		.id(2).gender(Gender.MALE)
		.birthDate(LocalDate.of(1981, 11, 1))
		.email("qwe@qwe.com")
		.phone("1231223")
		.address(TEST_ADDRESS)
		.group(TEST_GROUP)
		.build();
	Optional<Student> expected = Optional.of(expectedStudent);

	Optional<Student> actual = studentDao.findById(2);

	verify(groupDao).findById(2);
	verify(addressDao).findById(4);

	assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectStudentId_onFindById_shouldReturnEmptyOptional() {
	Optional<Student> expected = Optional.empty();

	Optional<Student> actual = studentDao.findById(5);

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasStudents_onFindAll_shouldReturnCorrectListOfStudents() {
	when(groupDao.findById(anyInt())).thenReturn(Optional.of(TEST_GROUP));
	when(addressDao.findById(anyInt())).thenReturn(Optional.of(TEST_ADDRESS));

	Student student1 = new Student.Builder("Ivan", "Petrov")
		.id(1).gender(Gender.MALE).birthDate(LocalDate.of(1980, 11, 1))
		.email("qwe@rty.com").phone("123123123").address(TEST_ADDRESS)
		.group(TEST_GROUP).build();
	Student student2 = new Student.Builder("John", "Doe")
		.id(2).gender(Gender.MALE).birthDate(LocalDate.of(1981, 11, 1))
		.email("qwe@qwe.com").phone("1231223").address(TEST_ADDRESS)
		.group(TEST_GROUP).build();
	Student student3 = new Student.Builder("Janna", "DArk")
		.id(3).gender(Gender.FEMALE).birthDate(LocalDate.of(1881, 11, 1))
		.email("qwe@no.fr").phone("1231223").address(TEST_ADDRESS)
		.group(TEST_GROUP).build();
	Student student4 = new Student.Builder("Mao", "Zedun")
		.id(4).gender(Gender.MALE).birthDate(LocalDate.of(1921, 9, 14))
		.email("qwe@no.cn").phone("1145223").address(TEST_ADDRESS)
		.group(TEST_GROUP).build();
	List<Student> expected = new ArrayList<>();
	expected.add(student1);
	expected.add(student2);
	expected.add(student3);
	expected.add(student4);

	List<Student> actual = studentDao.findAll();

	verify(groupDao, times(4)).findById(anyInt());
	verify(addressDao, times(4)).findById(anyInt());
	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasNoStudents_onFindAll_shouldReturnEmptyListOfStudents() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "students");

	List<Student> students = studentDao.findAll();

	assertThat(students).isEmpty();
    }

    @Test
    void givenStudent_onUpdate_shouldUpdateCorrectly() {
	Student student = new Student.Builder("Name", "Lastname")
		.id(2).gender(Gender.MALE)
		.birthDate(LocalDate.of(1980, 2, 2))
		.email("test@mail").phone("+phone")
		.address(TEST_ADDRESS).group(TEST_GROUP)
		.build();

	int rowsBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"students", "id = 2 AND " + TEST_WHERE_CLAUSE);

	studentDao.update(student);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"students", "id = 2 AND " + TEST_WHERE_CLAUSE);

	assertThat(rowsBeforeUpdate).isZero();
	assertThat(rowsAfterCreate).isEqualTo(1);
    }

    @Test
    void givenCorrectStudentId_onDelete_shouldDeleteCorrectly() {
	int rowsBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students", "id = 2");

	studentDao.delete(2);

	int rowsAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students", "id = 2");

	assertEquals(rowsAfterDelete, rowsBeforeDelete - 1);
    }
}
