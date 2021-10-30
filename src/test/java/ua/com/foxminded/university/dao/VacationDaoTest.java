package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.model.Vacation;

import java.time.LocalDate;
import java.util.*;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.dao.VacationDaoTest.TestData.*;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class VacationDaoTest {

    private static final String TEST_WHERE_CLAUSE = "start_date='2020-06-01' AND end_date='2020-07-01'";

    @Autowired
    private VacationDao vacationDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenNewVacation_onCreate_shouldCreateVacation() {
        int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "vacations", "id = 5 AND " + TEST_WHERE_CLAUSE);

        vacationDao.create(vacationToCreate);

        int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "vacations", "id = 5");

        assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

    @Test
    void givenCorrectVacationId_onFindById_shouldReturnOptionalWithCorrectVacation() {
        Optional<Vacation> expected = Optional.of(expectedVacation2);
        Optional<Vacation> actual = vacationDao.findById(2);

        assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectVacationId_onFindById_shouldReturnEmptyOptional() {
        Optional<Vacation> expected = Optional.empty();

        Optional<Vacation> actual = vacationDao.findById(5);

        assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasVacations_onFindAll_shouldReturnCorrectListOfVacations() {
        List<Vacation> actual = vacationDao.findAll();

        assertEquals(expectedVacations, actual);
    }

    @Test
    void ifDatabaseHasNoVacations_onFindAll_shouldReturnEmptyListOfVacations() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "vacations");

        List<Vacation> vacations = vacationDao.findAll();

        assertThat(vacations).isEmpty();
    }

    @Test
    void givenVacationWithExistingId_onUpdate_shouldUpdateCorrectly() {
        int rowsBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "vacations", "id = 2 AND " + TEST_WHERE_CLAUSE);

        vacationDao.update(vacationToUpdate);

        int rowsAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "vacations", "id = 2 AND " + TEST_WHERE_CLAUSE);

        assertThat(rowsBeforeUpdate).isZero();
        assertThat(rowsAfterUpdate).isEqualTo(1);
    }

//    @Test
//    void givenCorrectVacationId_onDelete_shouldDeleteCorrectly() {
//        int rowsBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "vacations", "id = 2");
//
//        vacationDao.delete(2);
//
//        int rowsAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "vacations", "id = 2");
//
//        assertEquals(rowsAfterDelete, rowsBeforeDelete - 1);
//    }

    public interface TestData {
        Vacation vacationToCreate = new Vacation(5, LocalDate.of(2020, 06, 01), LocalDate.of(2020, 07, 01));
        Vacation vacationToUpdate = new Vacation(2, LocalDate.of(2020, 06, 01), LocalDate.of(2020, 07, 01));

        Vacation expectedVacation1 = new Vacation(1, LocalDate.of(2000, 01, 01), LocalDate.of(2000, 02, 01));
        Vacation expectedVacation2 = new Vacation(2, LocalDate.of(2000, 05, 01), LocalDate.of(2000, 06, 01));
        Vacation expectedVacation3 = new Vacation(3, LocalDate.of(2000, 01, 15), LocalDate.of(2000, 02, 15));
        Vacation expectedVacation4 = new Vacation(4, LocalDate.of(2000, 06, 01), LocalDate.of(2000, 07, 01));
        List<Vacation> expectedVacations = new ArrayList<>(
                Arrays.asList(expectedVacation1, expectedVacation2, expectedVacation3, expectedVacation4));

        Vacation vacationGoingOverNewYear = new Vacation(LocalDate.of(2000, 12, 25), LocalDate.of(2001, 1, 10));

        Vacation intersectingVacation = new Vacation(5, LocalDate.of(2000, 02, 01), LocalDate.of(2000, 02, 05));

        Map<Integer, Long> daysByYearsMap = Map.ofEntries(entry(2000, 20L));
    }
}