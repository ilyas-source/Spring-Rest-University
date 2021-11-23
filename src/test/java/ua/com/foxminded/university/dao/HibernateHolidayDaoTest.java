package ua.com.foxminded.university.dao;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.SpringTestConfig;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
public class HibernateHolidayDaoTest {

//    @Autowired
//    private HibernateHolidayRepository holidayRepository;
//    @Autowired
//    private HibernateTemplate hibernateTemplate;
//
//    @Test
//    void givenNewHoliday_onCreate_shouldCreateHoliday() {
//        var actual = hibernateTemplate.get(Holiday.class, 4);
//        assertNull(actual);
//
//        holidayDao.create(holidayToCreate);
//
//        actual = hibernateTemplate.get(Holiday.class, 4);
//        assertEquals(holidayToCreate, actual);
//    }
//
//    @Test
//    void givenCorrectHolidayId_onFindById_shouldReturnOptionalWithCorrectHoliday() {
//        var expected = Optional.of(expectedHoliday2);
//
//        var actual = holidayDao.findById(2);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void givenIncorrectHolidayId_onFindById_shouldReturnEmptyOptional() {
//        Optional<Holiday> expected = Optional.empty();
//
//        var actual = holidayDao.findById(5);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void ifDatabaseHasHolidays_onFindAll_shouldReturnCorrectListOfHolidays() {
//        assertEquals(expectedHolidays, holidayDao.findAll());
//    }
//
//    @Test
//    void ifDatabaseHasNoHolidays_onFindAll_shouldReturnEmptyListOfHolidays() {
//        hibernateTemplate.deleteAll(expectedHolidays);
//
//        var holidays = holidayDao.findAll();
//
//        assertThat(holidays).isEmpty();
//    }
//
//    @Test
//    void givenHoliday_onUpdate_shouldUpdateCorrectly() {
//        holidayDao.update(holidayToUpdate);
//
//        var expected = hibernateTemplate.get(Holiday.class, 2);
//
//        assertEquals(holidayToUpdate, expected);
//    }
//
//    @Test
//    void givenCorrectHolidayId_onDelete_shouldDeleteCorrectly() {
//        holidayDao.delete(expectedHoliday2);
//
//        var expected = hibernateTemplate.get(Holiday.class, 2);
//        assertNull(expected);
//    }
//
//    @Test
//    void givenDate_onFindByDate_shouldReturnListWithCorrectHolidays() {
//        List<Holiday> expected = new ArrayList<>(List.of(expectedHoliday1));
//
//        List<Holiday> actual = holidayDao.findByDate(expectedHoliday1.getDate());
//
//        assertEquals(expected, actual);
//    }
//
//    public interface TestData {
//        Holiday holidayToCreate = new Holiday(4, LocalDate.of(2000, 1, 1), "test");
//        Holiday holidayToUpdate = new Holiday(2, LocalDate.of(2000, 1, 1), "test");
//
//        Holiday expectedHoliday1 = new Holiday(1, LocalDate.of(2000, 12, 25), "Christmas");
//        Holiday expectedHoliday2 = new Holiday(2, LocalDate.of(2000, 10, 30), "Halloween");
//        Holiday expectedHoliday3 = new Holiday(3, LocalDate.of(2000, 3, 8), "International womens day");
//
//        List<Holiday> expectedHolidays = new ArrayList<>(
//                Arrays.asList(expectedHoliday1, expectedHoliday2, expectedHoliday3));
//    }
}
