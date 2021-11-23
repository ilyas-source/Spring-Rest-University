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
public class HibernateLocationDaoTest {

//    @Autowired
//    private HibernateLocationDao locationDao;
//    @Autowired
//    private HibernateTemplate hibernateTemplate;
//
//    @Test
//    void givenNewLocation_onCreate_shouldCreateLocation() {
//        var actual = hibernateTemplate.get(Location.class, 4);
//        assertNull(actual);
//
//        locationDao.create(locationToCreate);
//
//        actual = hibernateTemplate.get(Location.class, 4);
//        assertEquals(locationToCreate, actual);
//    }
//
//    @Test
//    void givenCorrectLocationId_onFindById_shouldReturnOptionalWithCorrectLocation() {
//        var expected = Optional.of(expectedLocation2);
//
//        var actual = locationDao.findById(2);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void givenIncorrectLocationId_onFindById_shouldReturnEmptyOptional() {
//        Optional<Location> expected = Optional.empty();
//
//        var actual = locationDao.findById(5);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void ifDatabaseHasLocations_onFindAll_shouldReturnCorrectListOfLocations() {
//        assertEquals(expectedLocations, locationDao.findAll());
//    }
//
//    @Test
//    void ifDatabaseHasNoLocations_onFindAll_shouldReturnEmptyListOfLocations() {
//        hibernateTemplate.deleteAll(expectedLocations);
//
//        var locations = locationDao.findAll();
//
//        assertThat(locations).isEmpty();
//    }
//
//    @Test
//    void givenLocation_onUpdate_shouldUpdateCorrectly() {
//        locationDao.update(locationToUpdate);
//
//        var expected = hibernateTemplate.get(Location.class, 2);
//
//        assertEquals(locationToUpdate, expected);
//    }
//
//    @Test
//    void givenCorrectLocationId_onDelete_shouldDeleteCorrectly() {
//        locationDao.delete(expectedLocation2);
//
//        var expected = hibernateTemplate.get(Location.class, 2);
//        assertNull(expected);
//    }
//
//    public interface TestData {
//        Location locationToCreate = new Location(4, "test", 10, 100);
//        Location locationToUpdate = new Location(2, "test", 10, 100);
//
//        Location expectedLocation1 = new Location(1, "Phys building", 2, 22);
//        Location expectedLocation2 = new Location(2, "Chem building", 1, 12);
//        Location expectedLocation3 = new Location(3, "Chem building", 2, 12);
//
//        List<Location> expectedLocations = new ArrayList<>(
//                Arrays.asList(expectedLocation1, expectedLocation2, expectedLocation3));
//    }
}
