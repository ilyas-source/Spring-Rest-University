package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.model.Timeslot;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.*;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TimeslotDaoTest {

    private static final String TEST_WHERE_CLAUSE = "begin_time='12:00:00' AND end_time = '12:15:00'";

    @Autowired
    private TimeslotDao timeslotDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenNewTimeslot_onCreate_shouldCreateTimeslot() {
        int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "timeslots", "id = 4 AND " + TEST_WHERE_CLAUSE);

        timeslotDao.create(timeslotToCreate);

        int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "timeslots", "id = 4 AND " + TEST_WHERE_CLAUSE);

        assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

    @Test
    void givenCorrectTimeslotId_onFindById_shouldReturnOptionalWithCorrectTimeslot() {
        Optional<Timeslot> expected = Optional.of(expectedTimeslot2);

        Optional<Timeslot> actual = timeslotDao.findById(2);

        assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectTimeslotId_onFindById_shouldReturnEmptyOptional() {
        Optional<Timeslot> expected = Optional.empty();

        Optional<Timeslot> actual = timeslotDao.findById(5);

        assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasTimeslots_onFindAll_shouldReturnCorrectListOfTimeslots() {
        List<Timeslot> actual = timeslotDao.findAll();
        assertEquals(expectedTimeslots, actual);
    }

    @Test
    void ifDatabaseHasNoTimeslots_onFindAll_shouldReturnEmptyListOfTimeslots() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "timeslots");

        List<Timeslot> timeslots = timeslotDao.findAll();

        assertThat(timeslots).isEmpty();
    }

    @Test
    void givenTimeslot_onUpdate_shouldUpdateCorrectly() {
        timeslotDao.update(timeslotToUpdate);

        int rowsAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "timeslots", "id = 2 AND " + TEST_WHERE_CLAUSE);

        assertThat(rowsAfterUpdate).isEqualTo(1);
    }

    @Test
    void givenCorrectTimeslotId_onDelete_shouldDeleteCorrectly() {
        int rowsBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "timeslots", "id = 2");

        timeslotDao.delete(2);

        int rowsAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "timeslots", "id = 2");

        assertEquals(rowsAfterDelete, rowsBeforeDelete - 1);
    }

    public interface TestData {
        Timeslot timeslotToCreate = new Timeslot(4, LocalTime.of(12, 00), LocalTime.of(12, 15));
        Timeslot timeslotToUpdate = new Timeslot(2, LocalTime.of(12, 00), LocalTime.of(12, 15));

        Timeslot expectedTimeslot1 = new Timeslot(1, LocalTime.of(9, 00), LocalTime.of(9, 45));
        Timeslot expectedTimeslot2 = new Timeslot(2, LocalTime.of(10, 00), LocalTime.of(10, 45));
        Timeslot expectedTimeslot3 = new Timeslot(3, LocalTime.of(11, 00), LocalTime.of(11, 45));
        List<Timeslot> expectedTimeslots = new ArrayList<>(
                Arrays.asList(expectedTimeslot1, expectedTimeslot2, expectedTimeslot3));

        Timeslot timeslotWithBreaks = new Timeslot(LocalTime.of(8, 45), LocalTime.of(10, 00));
    }
}