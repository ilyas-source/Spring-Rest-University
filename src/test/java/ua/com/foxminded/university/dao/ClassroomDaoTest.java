package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.*;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ClassroomDaoTest {

    private static final String TEST_WHERE_CLAUSE = "location_id=4 AND name='Test room' AND capacity=5";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ClassroomDao classroomDao;

    @Test
    void givenName_onFindByName_shouldReturnOptionalwithCorrectClassroom() {
        Optional<Classroom> expected = Optional.of(expectedClassroom1);

        Optional<Classroom> actual = classroomDao.findByName(expectedClassroom1.getName());

        assertEquals(expected, actual);
    }

    @Test
    void givenLocation_onFindByLocation_shouldReturnOptionalwithCorrectClassroom() {
        Optional<Classroom> expected = Optional.of(expectedClassroom1);

        Optional<Classroom> actual = classroomDao.findByLocation(expectedClassroom1.getLocation());

        assertEquals(expected, actual);
    }

    @Test
    void givenNewClassroom_onCreate_shouldCreateClassroom() {
        int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "classrooms", TEST_WHERE_CLAUSE);

        classroomDao.create(classroomToCreate);

        int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "classrooms", TEST_WHERE_CLAUSE);

        assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

    @Test
    void givenClassroomWithExistingId_onUpdate_shouldUpdateCorrectly() {
        int rowsBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "classrooms", "id = 2 AND " + TEST_WHERE_CLAUSE);

        classroomDao.update(classroomToUpdate);

        int rowsAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "classrooms", "id = 2 AND " + TEST_WHERE_CLAUSE);

        assertThat(rowsBeforeUpdate).isZero();
        assertThat(rowsAfterUpdate).isEqualTo(1);
    }

    @Test
    void givenCorrectClassroomId_onFindById_shouldReturnOptionalWithCorrectClassroom() {
        Optional<Classroom> expected = Optional.of(expectedClassroom2);

        Optional<Classroom> actual = classroomDao.findById(2);

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
        List<Classroom> actual = classroomDao.findAll();

        assertEquals(expectedClassrooms, actual);
    }

    @Test
    void ifDatabaseHasNoClassrooms_onFindAll_shouldReturnEmptyListOfClassrooms() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "classrooms");

        List<Classroom> classrooms = classroomDao.findAll();

        assertThat(classrooms).isEmpty();
    }

    @Test
    void givenCorrectClassroomId_onDelete_shouldDeleteCorrectly() {
        int rowsBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "classrooms", "id = 2");

        classroomDao.delete(2);

        int rowsAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "classrooms", "id = 2");

        assertEquals(rowsAfterDelete, rowsBeforeDelete - 1);
    }

    public interface TestData {
        Location testLocation = new Location(4, "Test location", 1, 1);
        Classroom classroomToCreate = new Classroom(4, testLocation, "Test room", 5);
        Classroom classroomToUpdate = new Classroom(2, testLocation, "Test room", 5);

        Location location1 = new Location(1, "Phys building", 2, 22);
        Classroom expectedClassroom1 = new Classroom(1, location1, "Big physics auditory", 500);

        Location location2 = new Location(2, "Chem building", 1, 12);
        Classroom expectedClassroom2 = new Classroom(2, location2, "Small chemistry auditory", 30);

        Location location3 = new Location(3, "Chem building", 2, 12);
        Classroom expectedClassroom3 = new Classroom(3, location3, "Chemistry laboratory", 15);

        List<Classroom> expectedClassrooms = new ArrayList<>(
                Arrays.asList(expectedClassroom1, expectedClassroom2, expectedClassroom3));
    }
}