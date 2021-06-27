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
import ua.com.foxminded.university.dao.jdbc.JdbcLocationDao;
import ua.com.foxminded.university.model.Location;

//INSERT INTO locations (building, floor, room_number) VALUES
//('Phys building', 2, 22),
//('Chem building', 1, 12),
//('Chem building', 2, 12);

@SpringJUnitConfig(SpringTestConfig.class)
@Sql(scripts = { "classpath:schema.sql", "classpath:fill-locations.sql" })
class LocationDaoTest {

    private static final String TEST_WHERE_CLAUSE = "building='test' AND floor=10 and room_number=100";

    @Autowired
    private JdbcLocationDao locationDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenNewLocation_onCreate_shouldCreateLocation() {
	Location location = new Location(4, "test", 10, 100);
	int elementBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"locations", "id = 4 AND " + TEST_WHERE_CLAUSE);

	locationDao.create(location);

	int elementAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"locations", "id = 4 AND " + TEST_WHERE_CLAUSE);

	assertEquals(elementAfterCreate, elementBeforeCreate + 1);
    }

    @Test
    void givenCorrectLocationId_onFindById_shouldReturnOptionalWithCorrectLocation() {
	Optional<Location> expected = Optional.of(new Location(2, "Chem building", 1, 12));

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
	List<Location> expected = new ArrayList<>();
	expected.add(new Location(1, "Phys building", 2, 22));
	expected.add(new Location(2, "Chem building", 1, 12));
	expected.add(new Location(3, "Chem building", 2, 12));

	List<Location> actual = locationDao.findAll();

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasNoLocations_onFindAll_shouldReturnEmptyListOfLocations() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "locations");

	List<Location> locations = locationDao.findAll();

	assertThat(locations).isEmpty();
    }

    @Test
    void givenLocation_onUpdate_shouldUpdateCorrectly() {
	Location location = new Location(2, "test", 10, 100);

	locationDao.update(location);

	int elementAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"locations", "id = 2 AND " + TEST_WHERE_CLAUSE);

	assertThat(elementAfterUpdate).isEqualTo(1);
    }

    @Test
    void givenCorrectLocationId_onDelete_shouldDeleteCorrectly() {
	int elementBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "locations", "id = 2");

	locationDao.delete(2);

	int elementAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "locations", "id = 2");

	assertEquals(elementAfterDelete, elementBeforeDelete - 1);
    }
}
