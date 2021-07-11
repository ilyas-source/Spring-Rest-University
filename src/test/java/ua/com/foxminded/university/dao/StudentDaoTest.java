package ua.com.foxminded.university.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.dao.StudentDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.AddressDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.GroupDaoTest.TestData.*;

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
import ua.com.foxminded.university.dao.jdbc.JdbcStudentDao;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class StudentDaoTest {

    private static final String TEST_WHERE_CLAUSE = "first_name = 'Name' AND last_name = 'Lastname' AND gender = 'MALE' " +
	    "AND birth_date = '1980-02-02' AND email = 'test@mail' AND phone = '+phone' AND group_id = 2";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StudentDao studentDao;

    @Test
    void givenNewStudent_onCreate_shouldCreateStudent() {
	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"students", "id = 5 AND address_id = 3 AND " + TEST_WHERE_CLAUSE);

	studentDao.create(studentToCreate);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"students", "id = 5 AND address_id = 3 AND " + TEST_WHERE_CLAUSE);

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
	int rowsBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"students", "id = 2 AND " + TEST_WHERE_CLAUSE);

	studentDao.update(studentToUpdate);

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

    @Test
    void givenGroup_onCountStudentsInGroup_shouldReturnCorrectNumber() {
	int expected = 2;

	int actual = studentDao.countInGroup(expectedGroup1);

	assertEquals(expected, actual);
    }

    public interface TestData {
	Student studentToCreate = Student.builder().firstName("Name").lastName("Lastname")
		.id(5).gender(Gender.MALE).birthDate(LocalDate.of(1980, 2, 2))
		.email("test@mail").phone("+phone").address(expectedAddress3)
		.group(expectedGroup2).build();
	Student studentToUpdate = Student.builder().firstName("Name").lastName("Lastname")
		.id(2).gender(Gender.MALE).birthDate(LocalDate.of(1980, 2, 2))
		.email("test@mail").phone("+phone").address(expectedAddress3)
		.group(expectedGroup2).build();

	Student expectedStudent1 = Student.builder().firstName("Ivan").lastName("Petrov")
		.id(1).gender(Gender.MALE).birthDate(LocalDate.of(1980, 11, 1))
		.email("qwe@rty.com").phone("123123123").address(expectedAddress3)
		.group(expectedGroup1).build();
	Student expectedStudent2 = Student.builder().firstName("John").lastName("Doe")
		.id(2).gender(Gender.MALE).birthDate(LocalDate.of(1981, 11, 1))
		.email("qwe@qwe.com").phone("1231223").address(expectedAddress4)
		.group(expectedGroup2).build();
	Student expectedStudent3 = Student.builder().firstName("Janna").lastName("DArk")
		.id(3).gender(Gender.FEMALE).birthDate(LocalDate.of(1881, 11, 1))
		.email("qwe@no.fr").phone("1231223").address(expectedAddress5)
		.group(expectedGroup1).build();
	Student expectedStudent4 = Student.builder().firstName("Mao").lastName("Zedun")
		.id(4).gender(Gender.MALE).birthDate(LocalDate.of(1921, 9, 14))
		.email("qwe@no.cn").phone("1145223").address(expectedAddress6)
		.group(expectedGroup2).build();

	List<Student> expectedStudents = new ArrayList<>(
		Arrays.asList(expectedStudent1, expectedStudent2, expectedStudent3, expectedStudent4));

    }
}
