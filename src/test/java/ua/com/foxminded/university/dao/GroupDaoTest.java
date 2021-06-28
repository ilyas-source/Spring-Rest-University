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
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.model.Group;

@SpringJUnitConfig(SpringTestConfig.class)
@Sql(scripts = { "classpath:schema.sql", "classpath:fill-groups.sql" })
class GroupDaoTest {

    private static final String TEST_WHERE_CLAUSE = "name='test'";

    @Autowired
    private JdbcGroupDao groupDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenNewGroup_onCreate_shouldCreateGroup() {
	Group group = new Group(3, "test");
	int elementBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"groups", "id = 3 AND " + TEST_WHERE_CLAUSE);

	groupDao.create(group);

	int elementAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"groups", "id = 3 AND " + TEST_WHERE_CLAUSE);

	assertEquals(elementAfterCreate, elementBeforeCreate + 1);
    }

    @Test
    void givenCorrectGroupId_onFindById_shouldReturnOptionalWithCorrectGroup() {
	Optional<Group> expected = Optional.of(new Group(2, "ZI-08"));

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
	List<Group> expected = new ArrayList<>();
	expected.add(new Group(1, "AB-11"));
	expected.add(new Group(2, "ZI-08"));

	List<Group> actual = groupDao.findAll();

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasNoGroups_onFindAll_shouldReturnEmptyListOfGroups() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "groups");

	List<Group> groups = groupDao.findAll();

	assertThat(groups).isEmpty();
    }

    @Test
    void givenGroup_onUpdate_shouldUpdateCorrectly() {
	Group group = new Group(2, "test");

	groupDao.update(group);

	int elementAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"groups", "id = 2 AND " + TEST_WHERE_CLAUSE);

	assertThat(elementAfterUpdate).isEqualTo(1);
    }

    @Test
    void givenCorrectGroupId_onDelete_shouldDeleteCorrectly() {
	int elementBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", "id = 2");

	groupDao.delete(2);

	int elementAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", "id = 2");

	assertEquals(elementAfterDelete, elementBeforeDelete - 1);
    }
}
