package ua.com.foxminded.university.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.dao.StudentDaoTest.TestData.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import sun.security.util.math.IntegerFieldModuloP;
import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.dao.jdbc.mappers.StudentMapper;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

@SpringJUnitConfig(SpringTestConfig.class)
class StudentDaoTest {

    private static final String TEST_WHERE_CLAUSE = "first_name = 'Name' AND last_name = 'Lastname' AND gender = 'MALE' " +
	    "AND birth_date = '1980-02-02' AND email = 'test@mail' AND phone = '+phone' " +
	    "AND address_id = 4 AND group_id = 2";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private JdbcGroupDao groupDao;
    @Autowired
    private StudentDao studentDao;
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
	Optional<Student> expected = Optional.of(expectedStudent2);

	Optional<Student> actual = studentDao.findById(2);

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
	List<Student> actual = studentDao.findAll();

	assertEquals(expectedStudents, actual);
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

    interface TestData {

    }
}
