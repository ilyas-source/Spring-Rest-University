package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.model.Subject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.*;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SubjectDaoTest {

    private static final String TEST_WHERE_CLAUSE = "name='test' AND description = 'test'";

    @Autowired
    private SubjectDao subjectDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenName_onFindByName_shouldReturnOptionalwithCorrectSubject() {
        Optional<Subject> expected = Optional.of(expectedSubject1);

        Optional<Subject> actual = subjectDao.findByName(expectedSubject1.getName());

        assertEquals(expected, actual);
    }

    @Test
    void givenSubject_onCountAssignments_shouldReturnCount() {
        int actual = subjectDao.countAssignments(expectedSubject1);

        assertEquals(1, actual);
    }

    @Test
    void givenNewSubject_onCreate_shouldCreateSubject() {
        int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "subjects", "id = 5 AND " + TEST_WHERE_CLAUSE);

        subjectDao.create(subjectToCreate);

        int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "subjects", "id = 5 AND " + TEST_WHERE_CLAUSE);

        assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

    @Test
    void givenCorrectSubjectId_onFindById_shouldReturnOptionalWithCorrectSubject() {
        Optional<Subject> expected = Optional.of(expectedSubject2);

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
        List<Subject> actual = subjectDao.findAll();

        assertEquals(expectedSubjects, actual);
    }

    @Test
    void ifDatabaseHasNoSubjects_onFindAll_shouldReturnEmptyListOfSubjects() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "subjects");

        List<Subject> subjects = subjectDao.findAll();

        assertThat(subjects).isEmpty();
    }

    @Test
    void givenSubject_onUpdate_shouldUpdateCorrectly() {
        subjectDao.update(subjectToUpdate);

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
        List<Subject> actual = subjectDao.getByTeacherId(1);

        assertEquals(expectedSubjectsForTeacher1, actual);
    }

    @Test
    void givenIncorrectTeacherId_ongetSubjectsByTeacher_shouldReturnEmptyListOfSubjects() {

        List<Subject> actual = subjectDao.getByTeacherId(3);

        assertThat(actual).isEmpty();
    }

    public interface TestData {
        Subject subjectToCreate = new Subject(5, "test", "test");
        Subject subjectToUpdate = new Subject(2, "test", "test");

        Subject expectedSubject1 = new Subject(1, "Test Economics", "Base economics");
        Subject expectedSubject2 = new Subject(2, "Test Philosophy", "Base philosophy");
        Subject expectedSubject3 = new Subject(3, "Test Chemistry", "Base chemistry");
        Subject expectedSubject4 = new Subject(4, "Test Radiology", "Explore radiation");

        List<Subject> expectedSubjects = new ArrayList<>(
                Arrays.asList(expectedSubject1, expectedSubject2, expectedSubject3, expectedSubject4));

        List<Subject> expectedSubjectsForTeacher1 = new ArrayList<>(
                Arrays.asList(expectedSubject1, expectedSubject2));
    }
}
