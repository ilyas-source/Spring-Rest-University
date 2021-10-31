package ua.com.foxminded.university.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.hibernate.HibernateSubjectDao;
import ua.com.foxminded.university.model.Subject;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.*;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class SubjectDaoTest {

    private static final String TEST_WHERE_CLAUSE = "name='test' AND description = 'test'";

    @Autowired
    private HibernateSubjectDao subjectDao;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    void givenNewSubject_onCreate_shouldCreateSubject() {
        var actual = hibernateTemplate.get(Subject.class, 5);
        assertNull(actual);

        subjectDao.create(subjectToCreate);

        actual = hibernateTemplate.get(Subject.class, 5);
        assertEquals(subjectToCreate, actual);
    }

    @Test
    void givenCorrectSubjectId_onFindById_shouldReturnOptionalWithCorrectSubject() {
        var expected = Optional.of(expectedSubject2);

        var actual = subjectDao.findById(2);

        assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectSubjectId_onFindById_shouldReturnEmptyOptional() {
        Optional<Subject> expected = Optional.empty();

        var actual = subjectDao.findById(5);

        assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasSubjects_onFindAll_shouldReturnCorrectListOfSubjects() {
        assertEquals(expectedSubjects, subjectDao.findAll());
    }

    @Test
    void ifDatabaseHasNoSubjects_onFindAll_shouldReturnEmptyListOfSubjects() {
        hibernateTemplate.deleteAll(expectedSubjects);

        var subjects = subjectDao.findAll();

        assertThat(subjects).isEmpty();
    }

    @Test
    void givenSubject_onUpdate_shouldUpdateCorrectly() {
        subjectDao.update(subjectToUpdate);

        var expected = hibernateTemplate.get(Subject.class, 2);

        assertEquals(subjectToUpdate, expected);
    }

    @Test
    void givenCorrectSubjectId_onDelete_shouldDeleteCorrectly() {
        subjectDao.delete(expectedSubject2);

        var expected = hibernateTemplate.get(Subject.class, 2);
        assertNull(expected);
    }




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
