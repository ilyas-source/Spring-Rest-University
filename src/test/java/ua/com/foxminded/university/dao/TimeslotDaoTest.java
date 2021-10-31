package ua.com.foxminded.university.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.hibernate.HibernateTimeslotDao;
import ua.com.foxminded.university.model.Timeslot;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.*;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class TimeslotDaoTest {

    private static final String TEST_WHERE_CLAUSE = "begin_time='12:00:00' AND end_time = '12:15:00'";

    @Autowired
    private HibernateTimeslotDao timeslotDao;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    void givenNewTimeslot_onCreate_shouldCreateTimeslot() {
        var actual = hibernateTemplate.get(Timeslot.class, 3);
        assertNull(actual);

        timeslotDao.create(timeslotToCreate);

        actual = hibernateTemplate.get(Timeslot.class, 3);
        assertEquals(timeslotToCreate, actual);
    }

    @Test
    void givenCorrectTimeslotId_onFindById_shouldReturnOptionalWithCorrectTimeslot() {
        var expected = Optional.of(expectedTimeslot2);

        var actual = timeslotDao.findById(2);

        assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectTimeslotId_onFindById_shouldReturnEmptyOptional() {
        Optional<Timeslot> expected = Optional.empty();

        var actual = timeslotDao.findById(5);

        assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasTimeslots_onFindAll_shouldReturnCorrectListOfTimeslots() {
        assertEquals(expectedTimeslots, timeslotDao.findAll());
    }

    @Test
    void ifDatabaseHasNoTimeslots_onFindAll_shouldReturnEmptyListOfTimeslots() {
        hibernateTemplate.deleteAll(expectedTimeslots);

        var timeslots = timeslotDao.findAll();

        assertThat(timeslots).isEmpty();
    }

    @Test
    void givenTimeslot_onUpdate_shouldUpdateCorrectly() {
        timeslotDao.update(timeslotToUpdate);

        var expected = hibernateTemplate.get(Timeslot.class, 2);

        assertEquals(timeslotToUpdate, expected);
    }

    @Test
    void givenCorrectTimeslotId_onDelete_shouldDeleteCorrectly() {
        timeslotDao.delete(expectedTimeslot2);

        var expected = hibernateTemplate.get(Timeslot.class, 2);
        assertNull(expected);
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