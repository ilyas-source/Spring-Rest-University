package ua.com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
import ua.com.foxminded.university.menu.ClassroomsMenu;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;

//INSERT INTO classrooms (location_id, name, capacity) VALUES
//(1, 'Big physics auditory', 500),
//(2, 'Small chemistry auditory', 30),
//(3, 'Chemistry laboratory', 15);

//System.out.println(classroomMenu.getStringFromClassroom(expected.get()));
//System.out.println(classroomMenu.getStringFromClassroom(actual.get()));

@SpringJUnitConfig(SpringTestConfig.class)
@Sql(scripts = { "classpath:schema.sql", "classpath:test-data.sql" })
class ClassroomDaoTest {

    private static final String TEST_WHERE_CLAUSE = "location_id=5 AND name = 'test' AND capacity=5";

    @Autowired
    JdbcClassroomDao classroomDao;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    ClassroomsMenu classroomMenu; // todo delete after implementing tests

//    @Test
//    void givenNewClassroom_onCreate_shouldCreateClassroom() {
//	Location location = new Location(2, "Chem building", 1, 12);
//	Classroom classroom = new Classroom(4, location, TEST_WHERE_CLAUSE, 0)
//	int elementBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//		"classrooms", "id = 4 AND " + TEST_WHERE_CLAUSE);
//
//	classroomDao.create(classroom);
//
//	int elementAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//		"classrooms", "id = 4 AND " + TEST_WHERE_CLAUSE);
//
//	assertEquals(elementAfterCreate, elementBeforeCreate + 1);
//    }

//    @Test
//    void givenCorrectClassroomId_onFindById_shouldReturnOptionalWithCorrectClassroom() {
//	Optional<Classroom> expected = Optional
//		.of(new Classroom(2, 'Small chemistry auditory', 30));
//
//	Optional<Classroom> actual = classroomDao.findById(2);
//
//	assertEquals(expected, actual);
//    }

    @Test
    void givenIncorrectClassroomId_onFindById_shouldReturnEmptyOptional() {
	Optional<Classroom> expected = Optional.empty();

	Optional<Classroom> actual = classroomDao.findById(10);

	assertEquals(expected, actual);
    }

//    @Test
//    void ifDatabaseHasClassrooms_onFindAll_shouldReturnCorrectListOfClassroomes() {
//	List<Classroom> expected = new ArrayList<>();
//	expected.add(new Classroom(1, 1, "Big physics auditory", 500));
//	expected.add(new Classroom(2, 2, "Small chemistry auditory", 30));
//	expected.add(new Classroom(3, 3, "Big physics auditory", 500));
//
//	List<Classroom> actual = classroomDao.findAll();
//
//	assertEquals(expected, actual);
//    }

    @Test
    void ifDatabaseHasNoClassrooms_onFindAll_shouldReturnEmptyListOfClassroomes() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "classrooms");

	List<Classroom> classrooms = classroomDao.findAll();

	assertThat(classrooms).isEmpty();
    }

//    @Test
//    void givenClassroom_onUpdate_shouldUpdateCorrectly() {
//	Classroom classroom = new Classroom(2, 5, "test", 5);
//
//	classroomDao.update(classroom);
//
//	int elementAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//		"classrooms", "id = 2 AND " + TEST_WHERE_CLAUSE);
//
//	assertThat(elementAfterUpdate).isEqualTo(1);
//    }

    @Test
    void givenCorrectClassroomId_onDelete_shouldDeleteCorrectly() {
	int elementBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "classrooms", "id = 2");

	classroomDao.delete(2);

	int elementAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "classrooms", "id = 2");

	assertEquals(elementAfterDelete, elementBeforeDelete - 1);
    }
}