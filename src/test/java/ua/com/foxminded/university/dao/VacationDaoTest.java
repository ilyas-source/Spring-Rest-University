package ua.com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
import static org.assertj.core.api.Assertions.assertThat;

import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import ua.com.foxminded.university.dao.jdbc.JdbcVacationDao;
import ua.com.foxminded.university.dao.jdbc.mappers.VacationMapper;
import ua.com.foxminded.university.menu.TeachersMenu;
import ua.com.foxminded.university.menu.VacationsMenu;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(SpringTestConfig.class)
@Sql(scripts = { "classpath:schema.sql", "classpath:test-data.sql" })
class VacationDaoTest {

    private static final String TEST_WHERE_CLAUSE = "start_date='2020-06-01' AND end_date='2020-07-01'";

    @Mock
    private JdbcTeacherDao teacherDao;
    @InjectMocks
    @Autowired
    private JdbcVacationDao vacationDao;
    @InjectMocks
    @Autowired
    private VacationMapper vacationMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenNewVacation_onCreate_shouldCreateVacation() {
	Vacation vacation = new Vacation(5, LocalDate.of(2020, 06, 01), LocalDate.of(2020, 07, 01));
	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"vacations", "id = 5 AND " + TEST_WHERE_CLAUSE);

	vacationDao.create(vacation);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"vacations", "id = 5");

	assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

    @Test
    void givenCorrectVacationId_onFindById_shouldReturnOptionalWithCorrectVacation() {
	Optional<Vacation> expected = Optional
		.of(new Vacation(2, LocalDate.of(2000, 05, 01), LocalDate.of(2000, 06, 01)));
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
	List<Vacation> expected = new ArrayList<>();
	expected.add(new Vacation(1, LocalDate.of(2000, 01, 01), LocalDate.of(2000, 02, 01)));
	expected.add(new Vacation(2, LocalDate.of(2000, 05, 01), LocalDate.of(2000, 06, 01)));
	expected.add(new Vacation(3, LocalDate.of(2000, 01, 15), LocalDate.of(2000, 02, 15)));
	expected.add(new Vacation(4, LocalDate.of(2000, 06, 01), LocalDate.of(2000, 07, 01)));

	List<Vacation> actual = vacationDao.findAll();

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasNoVacations_onFindAll_shouldReturnEmptyListOfVacations() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "vacations");

	List<Vacation> vacations = vacationDao.findAll();

	assertThat(vacations).isEmpty();
    }

    @Test
    void givenVacationWithExistingId_onUpdate_shouldUpdateCorrectly() {
	Vacation vacation = new Vacation(2, LocalDate.of(2020, 06, 01), LocalDate.of(2020, 07, 01));

	int rowsBeforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"vacations", "id = 2 AND " + TEST_WHERE_CLAUSE);

	vacationDao.update(vacation);

	int rowsAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"vacations", "id = 2 AND " + TEST_WHERE_CLAUSE);

	assertThat(rowsBeforeUpdate).isZero();
	assertThat(rowsAfterUpdate).isEqualTo(1);
    }

    @Test
    void givenCorrectVacationId_onDelete_shouldDeleteCorrectly() {
	int rowsBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "vacations", "id = 2");

	vacationDao.delete(2);

	int rowsAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "vacations", "id = 2");

	assertEquals(rowsAfterDelete, rowsBeforeDelete - 1);
    }
}