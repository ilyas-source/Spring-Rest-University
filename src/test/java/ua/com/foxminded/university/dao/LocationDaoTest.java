package ua.com.foxminded.university.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.dao.LocationDaoTest.TestData.*;

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
import ua.com.foxminded.university.dao.jdbc.JdbcLocationDao;
import ua.com.foxminded.university.model.Location;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class LocationDaoTest {

    private static final String TEST_WHERE_CLAUSE = "building='test' AND floor=10 and room_number=100";

    @Autowired
    private JdbcLocationDao locationDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenNewLocation_onCreate_shouldCreateLocation() {
	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"locations", "id = 4 AND " + TEST_WHERE_CLAUSE);

	locationDao.create(locationToCreate);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"locations", "id = 4 AND " + TEST_WHERE_CLAUSE);

	assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

    @Test
    void givenCorrectLocationId_onFindById_shouldReturnOptionalWithCorrectLocation() {
	Optional<Location> expected = Optional.of(expectedLocation2);

	Optional<Location> actual = locationDao.findById(2);

	assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectLocationId_onFindById_shouldReturnEmptyOptional() {
	Optional<Location> expected = Optional.empty();

	Optional<Location> actual = locationDao.findById(5);

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasLocations_onFindAll_shouldReturnCorrectListOfLocations() {
	List<Location> actual = locationDao.findAll();

	assertEquals(expectedLocations, actual);
    }

    @Test
    void ifDatabaseHasNoLocations_onFindAll_shouldReturnEmptyListOfLocations() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "locations");

	List<Location> locations = locationDao.findAll();

	assertThat(locations).isEmpty();
    }

    @Test
    void givenLocation_onUpdate_shouldUpdateCorrectly() {
	locationDao.update(locationToUpdate);

	int rowsAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"locations", "id = 2 AND " + TEST_WHERE_CLAUSE);

	assertThat(rowsAfterUpdate).isEqualTo(1);
    }

    @Test
    void givenCorrectLocationId_onDelete_shouldDeleteCorrectly() {
	int rowsBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "locations", "id = 2");

	locationDao.delete(2);

	int rowsAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "locations", "id = 2");

	assertEquals(rowsAfterDelete, rowsBeforeDelete - 1);
    }

    public interface TestData {
	Location locationToCreate = new Location(4, "test", 10, 100);
	Location locationToUpdate = new Location(2, "test", 10, 100);

	Location expectedLocation1 = new Location(1, "Phys building", 2, 22);
	Location expectedLocation2 = new Location(2, "Chem building", 1, 12);
	Location expectedLocation3 = new Location(3, "Chem building", 2, 12);

	List<Location> expectedLocations = new ArrayList<>(
		Arrays.asList(expectedLocation1, expectedLocation2, expectedLocation3));
    }
}
