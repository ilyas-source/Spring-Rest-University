package ua.com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
import ua.com.foxminded.university.dao.jdbc.JdbcLocationDao;
import ua.com.foxminded.university.dao.jdbc.mappers.ClassroomMapper;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;

//INSERT INTO classrooms (location_id, name, capacity) VALUES
//(1, 'Big physics auditory', 500),
//(2, 'Small chemistry auditory', 30),
//(3, 'Chemistry laboratory', 15);

//INSERT INTO locations (building, floor, room_number) VALUES
//('Phys building', 2, 22),
//('Chem building', 1, 12),
//('Chem building', 2, 12);

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(SpringTestConfig.class)
@Sql(scripts = { "classpath:schema.sql", "classpath:test-data.sql" })
class ClassroomDaoTest {

    private static final String TEST_WHERE_CLAUSE = "location_id=4 AND name='Test room' AND capacity=5";
    private static final Location TEST_LOCATION = new Location(4, "Test location", 1, 1);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Mock
    private JdbcLocationDao locationDao;
    @InjectMocks
    @Autowired
    private JdbcClassroomDao classroomDao;
    @InjectMocks
    @Autowired
    private ClassroomMapper classroomMapper;

    @Test
    void givenNewClassroom_onCreate_shouldCreateClassroom() {
	Classroom classroom = new Classroom(TEST_LOCATION, "Test room", 5);

	int elementBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"classrooms", TEST_WHERE_CLAUSE);

	classroomDao.create(classroom);
	verify(locationDao).create(TEST_LOCATION);

	int elementAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"classrooms", TEST_WHERE_CLAUSE);

	assertEquals(elementAfterCreate, elementBeforeCreate + 1);
    }

    @Test
    void givenClassroomWithExistingId_onUpdate_shouldUpdateCorrectly() {
	Classroom classroom = new Classroom(2, TEST_LOCATION, "Test room", 5);

	int elementBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"classrooms", "id = 2 AND " + TEST_WHERE_CLAUSE);

	classroomDao.update(classroom);
	verify(locationDao).update(TEST_LOCATION);

	int elementAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"classrooms", "id = 2 AND " + TEST_WHERE_CLAUSE);

	assertThat(elementBeforeUpdate).isZero();
	assertThat(elementAfterUpdate).isEqualTo(1);
    }

    @Test
    void givenCorrectClassroomId_onFindById_shouldReturnOptionalWithCorrectClassroom() {
	Location location = new Location(2, "Chem building", 1, 12);
	when(locationDao.findById(2)).thenReturn(Optional.of(location));

	Optional<Classroom> expected = Optional.of(new Classroom(2, location, "Small chemistry auditory", 30));

	Optional<Classroom> actual = classroomDao.findById(2);
	verify(locationDao).findById(2);

	assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectClassroomId_onFindById_shouldReturnEmptyOptional() {
	Optional<Classroom> expected = Optional.empty();

	Optional<Classroom> actual = classroomDao.findById(10);

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasClassrooms_onFindAll_shouldReturnCorrectListOfClassrooms() {
	List<Classroom> expected = new ArrayList<>();

	Location location1 = new Location(1, "Phys building", 2, 22);
	expected.add(new Classroom(1, location1, "Big physics auditory", 500));
	when(locationDao.findById(1)).thenReturn(Optional.of(location1));

	Location location2 = new Location(2, "Chem building", 1, 12);
	expected.add(new Classroom(2, location2, "Small chemistry auditory", 30));
	when(locationDao.findById(2)).thenReturn(Optional.of(location2));

	Location location3 = new Location(3, "Chem building", 2, 12);
	expected.add(new Classroom(3, location3, "Chemistry laboratory", 15));
	when(locationDao.findById(3)).thenReturn(Optional.of(location3));

	List<Classroom> actual = classroomDao.findAll();

	verify(locationDao, times(3)).findById(anyInt());

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasNoClassrooms_onFindAll_shouldReturnEmptyListOfClassrooms() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "classrooms");

	List<Classroom> classrooms = classroomDao.findAll();

	assertThat(classrooms).isEmpty();
    }

    @Test
    void givenCorrectClassroomId_onDelete_shouldDeleteCorrectly() {
	int elementBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "classrooms", "id = 2");

	classroomDao.delete(2);

	int elementAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "classrooms", "id = 2");

	assertEquals(elementAfterDelete, elementBeforeDelete - 1);
    }
}