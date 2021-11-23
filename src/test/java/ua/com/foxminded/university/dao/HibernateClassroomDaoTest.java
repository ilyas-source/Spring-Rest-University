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
public class HibernateClassroomDaoTest {

//    @Autowired
//    private HibernateClassroomDao classroomDao;
//    @Autowired
//    private HibernateTemplate hibernateTemplate;
//
//    @Test
//    void givenNewClassroom_onCreate_shouldCreateClassroom() {
//        var actual = hibernateTemplate.get(Classroom.class, 4);
//        assertNull(actual);
//
//        classroomDao.create(classroomToCreate);
//
//        actual = hibernateTemplate.get(Classroom.class, 4);
//        assertEquals(classroomToCreate, actual);
//    }
//
//    @Test
//    void givenCorrectClassroomId_onFindById_shouldReturnOptionalWithCorrectClassroom() {
//        var expected = Optional.of(expectedClassroom2);
//
//        var actual = classroomDao.findById(2);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void givenIncorrectClassroomId_onFindById_shouldReturnEmptyOptional() {
//        Optional<Classroom> expected = Optional.empty();
//
//        var actual = classroomDao.findById(5);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void ifDatabaseHasClassrooms_onFindAll_shouldReturnCorrectListOfClassrooms() {
//        assertEquals(expectedClassrooms, classroomDao.findAll());
//    }
//
//    @Test
//    void ifDatabaseHasNoClassrooms_onFindAll_shouldReturnEmptyListOfClassrooms() {
//        hibernateTemplate.deleteAll(expectedClassrooms);
//
//        var classrooms = classroomDao.findAll();
//
//        assertThat(classrooms).isEmpty();
//    }
//
//    @Test
//    void givenClassroom_onUpdate_shouldUpdateCorrectly() {
//        classroomDao.update(classroomToUpdate);
//
//        var expected = hibernateTemplate.get(Classroom.class, 2);
//
//        assertEquals(classroomToUpdate, expected);
//    }
//
//    @Test
//    void givenCorrectClassroomId_onDelete_shouldDeleteCorrectly() {
//        classroomDao.delete(expectedClassroom2);
//
//        var expected = hibernateTemplate.get(Classroom.class, 2);
//        assertNull(expected);
//    }
//
//    @Test
//    void givenName_onFindByName_shouldReturnOptionalWithCorrectClassroom() {
//        Optional<Classroom> expected = Optional.of(expectedClassroom1);
//
//        Optional<Classroom> actual = classroomDao.findByName(expectedClassroom1.getName());
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void givenWrongName_onFindByName_shouldReturnOptionalEmpty() {
//        Optional<Classroom> actual = classroomDao.findByName("wrong name");
//
//        assertEquals(Optional.empty(), actual);
//    }
//
//    @Test
//    void givenLocation_onFindByLocation_shouldReturnOptionalWithCorrectClassroom() {
//        var expected = Optional.of(expectedClassroom1);
//        var actual = classroomDao.findByLocation(expectedClassroom1.getLocation());
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void givenWrongLocation_onFindByLocation_shouldReturnOptionalEmpty() {
//        var actual = classroomDao.findByLocation(testLocation);
//
//        assertEquals(Optional.empty(), actual);
//    }
//
//    public interface TestData {
//        Location testLocation = new Location(4, "Test location", 1, 1);
//        Classroom classroomToCreate = new Classroom(4, testLocation, "Test room", 5);
//        Classroom classroomToUpdate = new Classroom(2, testLocation, "Test room", 5);
//
//        Location location1 = new Location(1, "Phys building", 2, 22);
//        Classroom expectedClassroom1 = new Classroom(1, location1, "Big physics auditory", 500);
//
//        Location location2 = new Location(2, "Chem building", 1, 12);
//        Classroom expectedClassroom2 = new Classroom(2, location2, "Small chemistry auditory", 30);
//
//        Location location3 = new Location(3, "Chem building", 2, 12);
//        Classroom expectedClassroom3 = new Classroom(3, location3, "Chemistry laboratory", 15);
//
//        List<Classroom> expectedClassrooms = new ArrayList<>(
//                Arrays.asList(expectedClassroom1, expectedClassroom2, expectedClassroom3));
//    }
//

}