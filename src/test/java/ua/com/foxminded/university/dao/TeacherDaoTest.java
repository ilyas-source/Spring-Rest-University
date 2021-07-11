package ua.com.foxminded.university.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.AddressDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.StudentDaoTest.TestData.expectedStudent1;
import static ua.com.foxminded.university.dao.VacationDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.*;

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
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TeacherDaoTest {

    private static final String TEST_WHERE_CLAUSE = "first_name='Test' AND last_name='Teacher' AND gender='MALE' AND degree='DOCTOR' AND email='test@mail' AND phone='phone'";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TeacherDao teacherDao;

    @Test
    void givenAddressId_onFindByAddressId_shouldReturnOptionalwithCorrectTeacher() {
	Optional<Teacher> expected = Optional.of(expectedTeacher1);

	Optional<Teacher> actual = teacherDao.findByAddressId(1);

	assertEquals(expected, actual);
    }

    @Test
    void givenNameAndEmail_onFindByNameAndEmail_shouldReturnOptionalwithCorrectTeacher() {
	Optional<Teacher> expected = Optional.of(expectedTeacher1);

	Optional<Teacher> actual = teacherDao.findByNameAndEmail("Adam", "Smith", "adam@smith.com");

	assertEquals(expected, actual);
    }

    @Test
    void givenNewTeacher_onCreate_shouldCreateTeacher() {
	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"teachers", "id = 3 AND address_id=7 AND " + TEST_WHERE_CLAUSE);

	teacherDao.create(teacherToCreate);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"teachers", "id = 3 AND address_id=7 AND " + TEST_WHERE_CLAUSE);

	assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

    @Test
    void givenCorrectTeacherId_onFindById_shouldReturnOptionalWithCorrectTeacher() {
	Optional<Teacher> expected = Optional.of(expectedTeacher2);

	Optional<Teacher> actual = teacherDao.findById(2);

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
	List<Teacher> actual = teacherDao.findAll();

	assertEquals(expectedTeachers, actual);
    }

    @Test
    void ifDatabaseHasNoTeachers_onFindAll_shouldReturnEmptyListOfTeachers() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "teachers");

	List<Teacher> teachers = teacherDao.findAll();

	assertThat(teachers).isEmpty();
    }

    @Test
    void givenTeacher_onUpdate_shouldUpdateCorrectly() {
	int rowsBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"teachers", "id = 2 AND address_id=2 AND " + TEST_WHERE_CLAUSE);

	teacherDao.update(teacherToUpdate);

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

    public interface TestData {
	List<Subject> testSubjects = new ArrayList<>(Arrays.asList(subjectToUpdate));
	List<Vacation> vacationsToCreate = new ArrayList<Vacation>(Arrays.asList(vacationToCreate));
	List<Vacation> vacationsToUpdate = new ArrayList<Vacation>(Arrays.asList(vacationToUpdate));

	Teacher teacherToCreate = Teacher.builder().firstName("Test").lastName("Teacher").id(3)
		.gender(Gender.MALE).degree(Degree.DOCTOR).subjects(testSubjects)
		.email("test@mail").phoneNumber("phone").address(addressToCreate)
		.vacations(vacationsToCreate).build();

	Teacher teacherToUpdate = Teacher.builder().firstName("Test").lastName("Teacher").id(2)
		.gender(Gender.MALE).degree(Degree.DOCTOR).subjects(testSubjects)
		.email("test@mail").phoneNumber("phone").address(addressToUpdate)
		.vacations(vacationsToUpdate).build();

	List<Vacation> expectedVacations1 = new ArrayList<>(Arrays.asList(expectedVacation1, expectedVacation2));
	List<Vacation> expectedVacations2 = new ArrayList<>(Arrays.asList(expectedVacation3, expectedVacation4));
	List<Subject> expectedSubjects1 = new ArrayList<>(Arrays.asList(expectedSubject1, expectedSubject2));
	List<Subject> expectedSubjects2 = new ArrayList<>(Arrays.asList(expectedSubject3, expectedSubject4));
	Teacher expectedTeacher1 = Teacher.builder().firstName("Adam").lastName("Smith").id(1)
		.gender(Gender.MALE).degree(Degree.DOCTOR).subjects(expectedSubjects1)
		.email("adam@smith.com").phoneNumber("+223322").address(expectedAddress1)
		.vacations(expectedVacations1).build();
	Teacher expectedTeacher2 = Teacher.builder().firstName("Marie").lastName("Curie").id(2)
		.gender(Gender.FEMALE).degree(Degree.MASTER).subjects(expectedSubjects2)
		.email("marie@curie.com").phoneNumber("+322223").address(expectedAddress2)
		.vacations(expectedVacations2).build();
	List<Teacher> expectedTeachers = new ArrayList<>(Arrays.asList(expectedTeacher1, expectedTeacher2));
    }
}
