package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.VacationDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.jdbc.JdbcVacationDao;

@ExtendWith(MockitoExtension.class)
class VacationServiceTest {

    @Mock
    private JdbcVacationDao vacationDao;
    @InjectMocks
    private VacationService vacationService;

    @Test
    void onFindAll_shouldReturnAllVacations() {
	when(vacationDao.findAll()).thenReturn(expectedVacations);

	assertEquals(expectedVacations, vacationService.findAll());
    }

    @Test
    void givenCorrectId_onFindById_shouldReturnOptionalWithCorrectVacation() {
	when(vacationDao.findById(1)).thenReturn(Optional.of(expectedVacation1));

	assertEquals(Optional.of(expectedVacation1), vacationService.findById(1));
    }

    @Test
    void givenVacation_onCreate_shouldCallCreate() {
	vacationService.create(expectedVacation1);

	verify(vacationDao).create(expectedVacation1);
    }

    @Test
    void givenVacation_onUpdate_shouldCallUpdate() {
	vacationService.update(expectedVacation1);

	verify(vacationDao).update(expectedVacation1);
    }

    @Test
    void givenVacation_onDelete_shouldCallDelete() {
	vacationService.delete(1);

	verify(vacationDao).delete(1);
    }
}