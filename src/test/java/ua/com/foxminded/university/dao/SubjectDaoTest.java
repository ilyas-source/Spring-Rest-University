package ua.com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import ua.com.foxminded.university.menu.SubjectsMenu;
import ua.com.foxminded.university.model.Subject;

@SpringJUnitConfig(SpringTestConfig.class)
@Sql(scripts = { "classpath:schema.sql", "classpath:test-data.sql" })
class SubjectDaoTest {

    private static final String TEST_WHERE_CLAUSE = "name='test' AND description = 'test'";

    @Autowired
    private JdbcSubjectDao subjectDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenNewSubject_onCreate_shouldCreateSubject() {
	Subject subject = new Subject(5, "test", "test");
	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"subjects", "id = 5 AND " + TEST_WHERE_CLAUSE);

	subjectDao.create(subject);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"subjects", "id = 5 AND " + TEST_WHERE_CLAUSE);

	assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

    @Test
    void givenCorrectSubjectId_onFindById_shouldReturnOptionalWithCorrectSubject() {
	Optional<Subject> expected = Optional.of(new Subject(2, "Test Philosophy", "Base philosophy"));

	Optional<Subject> actual = subjectDao.findById(2);

	assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectSubjectId_onFindById_shouldReturnEmptyOptional() {
	Optional<Subject> expected = Optional.empty();

	Optional<Subject> actual = subjectDao.findById(5);

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasSubjects_onFindAll_shouldReturnCorrectListOfSubjects() {
	List<Subject> expected = new ArrayList<>();
	expected.add(new Subject(1, "Test Economics", "Base economics"));
	expected.add(new Subject(2, "Test Philosophy", "Base philosophy"));
	expected.add(new Subject(3, "Test Chemistry", "Base chemistry"));
	expected.add(new Subject(4, "Test Radiology", "Explore radiation"));

	List<Subject> actual = subjectDao.findAll();

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasNoSubjects_onFindAll_shouldReturnEmptyListOfSubjects() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "subjects");

	List<Subject> subjects = subjectDao.findAll();

	assertThat(subjects).isEmpty();
    }

    @Test
    void givenSubject_onUpdate_shouldUpdateCorrectly() {
	Subject subject = new Subject(2, "test", "test");

	subjectDao.update(subject);

	int rowsAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"subjects", "id = 2 AND " + TEST_WHERE_CLAUSE);

	assertThat(rowsAfterUpdate).isEqualTo(1);
    }

    @Test
    void givenCorrectSubjectId_onDelete_shouldDeleteCorrectly() {
	int rowsBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "subjects", "id = 2");

	subjectDao.delete(2);

	int rowsAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "subjects", "id = 2");

	assertEquals(rowsAfterDelete, rowsBeforeDelete - 1);
    }

    @Test
    void givenCorrectTeacherId_ongetSubjectsByTeacher_shouldReturnCorrectListOfSubjects() {
	List<Subject> expected = new ArrayList<>();
	expected.add(new Subject(1, "Test Economics", "Base economics"));
	expected.add(new Subject(2, "Test Philosophy", "Base philosophy"));

	List<Subject> actual = subjectDao.getSubjectsByTeacherId(1);

	assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectTeacherId_ongetSubjectsByTeacher_shouldReturnEmptyListOfSubjects() {

	List<Subject> actual = subjectDao.getSubjectsByTeacherId(3);

	assertThat(actual).isEmpty();
    }
}
