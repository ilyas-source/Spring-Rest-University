package ua.com.foxminded.university.dao;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.com.foxminded.university.SpringTestConfig;

import javax.transaction.Transactional;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
public class HibernateSubjectDaoTest {

//    @Autowired
//    private HibernateSubjectDao subjectDao;
//    @Autowired
//    private HibernateTemplate hibernateTemplate;
//
//    @Test
//    void givenNewSubject_onCreate_shouldCreateSubject() {
//        var actual = hibernateTemplate.get(Subject.class, 5);
//        assertNull(actual);
//
//        subjectDao.create(subjectToCreate);
//
//        actual = hibernateTemplate.get(Subject.class, 5);
//        assertEquals(subjectToCreate, actual);
//    }
//
//    @Test
//    void givenCorrectSubjectId_onFindById_shouldReturnOptionalWithCorrectSubject() {
//        var expected = Optional.of(expectedSubject2);
//
//        var actual = subjectDao.findById(2);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void givenIncorrectSubjectId_onFindById_shouldReturnEmptyOptional() {
//        Optional<Subject> expected = Optional.empty();
//
//        var actual = subjectDao.findById(5);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void ifDatabaseHasSubjects_onFindAll_shouldReturnCorrectListOfSubjects() {
//        assertEquals(expectedSubjects, subjectDao.findAll());
//    }
//
//    @Test
//    void ifDatabaseHasNoSubjects_onFindAll_shouldReturnEmptyListOfSubjects() {
//        hibernateTemplate.deleteAll(expectedSubjects);
//
//        var subjects = subjectDao.findAll();
//
//        assertThat(subjects).isEmpty();
//    }
//
//    @Test
//    void givenSubject_onUpdate_shouldUpdateCorrectly() {
//        subjectDao.update(subjectToUpdate);
//
//        var expected = hibernateTemplate.get(Subject.class, 2);
//
//        assertEquals(subjectToUpdate, expected);
//    }
//
//    @Test
//    void givenCorrectSubjectId_onDelete_shouldDeleteCorrectly() {
//        subjectDao.delete(expectedSubject2);
//
//        var expected = hibernateTemplate.get(Subject.class, 2);
//        assertNull(expected);
//    }
//
//
//    @Test
//    void givenName_onFindByName_shouldReturnOptionalwithCorrectSubject() {
//        Optional<Subject> expected = Optional.of(expectedSubject1);
//
//        Optional<Subject> actual = subjectDao.findByName(expectedSubject1.getName());
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void givenWrongName_onFindByName_shouldReturnOptionalEmpty() {
//        Optional<Subject> actual = subjectDao.findByName("Wrong name");
//
//        assertEquals(Optional.empty(), actual);
//    }
//
//    @Test
//    void givenSubject_onCountAssignments_shouldReturnNumberOfAssignments() {
//        var actual = subjectDao.countAssignments(expectedSubject1);
//
//        assertEquals(1, actual);
//    }
//
//    @Test
//    void givenUnassignedSubject_onCountAssignments_shouldReturnZero() {
//        var actual = subjectDao.countAssignments(subjectToCreate);
//
//        assertEquals(0, actual);
//    }
//
//
//    public interface TestData {
//        Subject subjectToCreate = new Subject(5, "test", "test");
//        Subject subjectToUpdate = new Subject(2, "test", "test");
//
//        Subject expectedSubject1 = new Subject(1, "Test Economics", "Base economics");
//        Subject expectedSubject2 = new Subject(2, "Test Philosophy", "Base philosophy");
//        Subject expectedSubject3 = new Subject(3, "Test Chemistry", "Base chemistry");
//        Subject expectedSubject4 = new Subject(4, "Test Radiology", "Explore radiation");
//
//        List<Subject> expectedSubjects = new ArrayList<>(
//                Arrays.asList(expectedSubject1, expectedSubject2, expectedSubject3, expectedSubject4));
//    }
}
