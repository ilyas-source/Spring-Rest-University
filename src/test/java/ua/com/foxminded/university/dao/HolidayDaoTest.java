package ua.com.foxminded.university.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.dao.HolidayDaoTest.TestData.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import ua.com.foxminded.university.model.Holiday;

@SpringJUnitConfig(SpringTestConfig.class)
@Sql(scripts = { "classpath:schema.sql", "classpath:test-data.sql" })
class HolidayDaoTest {

    private static final String TEST_WHERE_CLAUSE = "date='2000-01-01' AND name = 'test'";

    @Autowired
    private JdbcHolidayDao holidayDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenNewHoliday_onCreate_shouldCreateHoliday() {
	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"holidays", "id = 4 AND " + TEST_WHERE_CLAUSE);

	holidayDao.create(holidayToCreate);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"holidays", "id = 4 AND " + TEST_WHERE_CLAUSE);

	assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

    @Test
    void givenCorrectHolidayId_onFindById_shouldReturnOptionalWithCorrectHoliday() {
	Optional<Holiday> expected = Optional.of(expectedHoliday2);

	Optional<Holiday> actual = holidayDao.findById(2);

	assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectHolidayId_onFindById_shouldReturnEmptyOptional() {
	Optional<Holiday> expected = Optional.empty();

	Optional<Holiday> actual = holidayDao.findById(5);

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasHolidays_onFindAll_shouldReturnCorrectListOfHolidays() {
	List<Holiday> actual = holidayDao.findAll();

	assertEquals(expectedHolidays, actual);
    }

    @Test
    void ifDatabaseHasNoHolidays_onFindAll_shouldReturnEmptyListOfHolidays() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "holidays");

	List<Holiday> holidays = holidayDao.findAll();

	assertThat(holidays).isEmpty();
    }

    @Test
    void givenHoliday_onUpdate_shouldUpdateCorrectly() {
	holidayDao.update(holidayToUpdate);

	int rowsAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"holidays", "id = 2 AND " + TEST_WHERE_CLAUSE);

	assertThat(rowsAfterUpdate).isEqualTo(1);
    }

    @Test
    void givenCorrectHolidayId_onDelete_shouldDeleteCorrectly() {
	int rowsBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "holidays", "id = 2");

	holidayDao.delete(2);

	int rowsAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "holidays", "id = 2");

	assertEquals(rowsAfterDelete, rowsBeforeDelete - 1);
    }

    interface TestData {
	Holiday holidayToCreate = new Holiday(4, LocalDate.of(2000, 01, 01), "test");
	Holiday holidayToUpdate = new Holiday(2, LocalDate.of(2000, 01, 01), "test");

	Holiday expectedHoliday1 = new Holiday(1, LocalDate.of(2000, 12, 25), "Christmas");
	Holiday expectedHoliday2 = new Holiday(2, LocalDate.of(2000, 10, 30), "Halloween");
	Holiday expectedHoliday3 = new Holiday(3, LocalDate.of(2000, 03, 8), "International womens day");

	List<Holiday> expectedHolidays = new ArrayList<>(
		Arrays.asList(expectedHoliday1, expectedHoliday2, expectedHoliday3));
    }
}
