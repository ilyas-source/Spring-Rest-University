package ua.com.foxminded.university.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.hibernate.HibernateVacationDao;
import ua.com.foxminded.university.model.Vacation;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ua.com.foxminded.university.dao.HibernateTeacherDaoTest.TestData.expectedTeacher1;
import static ua.com.foxminded.university.dao.HibernateVacationDaoTest.TestData.*;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class HibernateVacationDaoTest {

    @Autowired
    private HibernateVacationDao vacationDao;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    void givenNewVacation_onCreate_shouldCreateVacation() {
        var actual = hibernateTemplate.get(Vacation.class, 5);
        assertNull(actual);

        vacationDao.create(vacationToCreate);

        actual = hibernateTemplate.get(Vacation.class, 5);
        assertEquals(vacationToCreate, actual);
    }

    @Test
    void givenCorrectVacationId_onFindById_shouldReturnOptionalWithCorrectVacation() {
        var expected = Optional.of(expectedVacation2);

        var actual = vacationDao.findById(2);

        assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectVacationId_onFindById_shouldReturnEmptyOptional() {
        Optional<Vacation> expected = Optional.empty();

        var actual = vacationDao.findById(5);

        assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasVacations_onFindAll_shouldReturnCorrectListOfVacations() {
        assertEquals(expectedVacations, vacationDao.findAll());
    }

    @Test
    void ifDatabaseHasNoVacations_onFindAll_shouldReturnEmptyListOfVacations() {
        hibernateTemplate.deleteAll(expectedVacations);

        var vacations = vacationDao.findAll();

        assertThat(vacations).isEmpty();
    }

    @Test
    void givenVacation_onUpdate_shouldUpdateCorrectly() {
        vacationDao.update(vacationToUpdate);

        var actual = hibernateTemplate.get(Vacation.class, 2);

        assertEquals(vacationToUpdate, actual);
    }

    @Test
    void givenCorrectVacationId_onDelete_shouldDeleteCorrectly() {
        vacationDao.delete(expectedVacation2);

        var expected = hibernateTemplate.get(Vacation.class, 2);
        assertNull(expected);
    }

    @Test
    void givenTeacher_onFindByTeacher_shouldReturnCorrectListWithVacations() {
        var expected = new ArrayList<>(Arrays.asList(expectedVacation1, expectedVacation2));

        var actual = vacationDao.findByTeacher(expectedTeacher1);

        assertEquals(expected, actual);
    }

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

        Map<Integer, Long> daysByYearsMap = Map.ofEntries(entry(2000, 20L));
    }
}