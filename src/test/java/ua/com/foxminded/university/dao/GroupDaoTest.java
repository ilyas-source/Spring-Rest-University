package ua.com.foxminded.university.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.dao.GroupDaoTest.TestData.*;

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
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.model.Group;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GroupDaoTest {

    private static final String TEST_WHERE_CLAUSE = "name='test'";

    @Autowired
    private JdbcGroupDao groupDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenNewGroup_onCreate_shouldCreateGroup() {
	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"groups", "id = 3 AND " + TEST_WHERE_CLAUSE);

	groupDao.create(groupToCreate);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"groups", "id = 3 AND " + TEST_WHERE_CLAUSE);

	assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

    @Test
    void givenCorrectGroupId_onFindById_shouldReturnOptionalWithCorrectGroup() {
	Optional<Group> expected = Optional.of(expectedGroup2);

	Optional<Group> actual = groupDao.findById(2);

	assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectGroupId_onFindById_shouldReturnEmptyOptional() {
	Optional<Group> expected = Optional.empty();

	Optional<Group> actual = groupDao.findById(5);

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasGroups_onFindAll_shouldReturnCorrectListOfGroups() {
	List<Group> actual = groupDao.findAll();

	assertEquals(expectedGroups, actual);
    }

    @Test
    void ifDatabaseHasNoGroups_onFindAll_shouldReturnEmptyListOfGroups() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "groups");

	List<Group> groups = groupDao.findAll();

	assertThat(groups).isEmpty();
    }

    @Test
    void givenGroup_onUpdate_shouldUpdateCorrectly() {
	groupDao.update(groupToUpdate);

	int rowsAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"groups", "id = 2 AND " + TEST_WHERE_CLAUSE);

	assertThat(rowsAfterUpdate).isEqualTo(1);
    }

    @Test
    void givenCorrectGroupId_onDelete_shouldDeleteCorrectly() {
	int rowsBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", "id = 2");

	groupDao.delete(2);

	int rowsAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", "id = 2");

	assertEquals(rowsAfterDelete, rowsBeforeDelete - 1);
    }

    public interface TestData {
	Group groupToCreate = new Group(3, "test");
	Group groupToUpdate = new Group(2, "test");

	Group expectedGroup1 = new Group(1, "AB-11");
	Group expectedGroup2 = new Group(2, "ZI-08");

	List<Group> expectedGroups = new ArrayList<>(
		Arrays.asList(expectedGroup1, expectedGroup2));
    }
}
