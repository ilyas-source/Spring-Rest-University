package ua.com.foxminded.university.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.VacationDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.VacationDao;

@ExtendWith(MockitoExtension.class)
class VacationServiceTest {

    @Mock
    private VacationDao vacationDao;
    @InjectMocks
    private VacationService vacationService;

    @Test
    void onFindAll_shouldCallDaoFindAll() {
	vacationService.findAll();

	verify(vacationDao).findAll();
    }

    @Test
    void givenId_onFindById_shouldCallDaoFindById() {
	vacationService.findById(1);

	verify(vacationDao).findById(1);
    }

    @Test
    void givenVacation_onCreate_shouldCallCreate() {
	vacationService.create(expectedVacation1);

	verify(vacationDao).create(expectedVacation1);
    }

    @Test
    void givenIntersectingVacation_onCreate_shoultNotCallCreate() {
	when(vacationDao.findAll()).thenReturn(expectedVacations);

	vacationService.create(expectedVacation1);

	verify(vacationDao, never()).create(expectedVacation1);
    }

    @Test
    void givenVacation_onUpdate_shouldCallUpdate() {
	vacationService.update(expectedVacation1);

	verify(vacationDao).update(expectedVacation1);
    }

    @Test
    void givenIncorrectVacationId_onDelete_shouldNotCallDelete() {
	vacationService.delete(1);

	verify(vacationDao, never()).delete(1);
    }

    @Test
    void givenCorrectVacationId_onDelete_shouldCallDelete() {
	when(vacationDao.findById(1)).thenReturn(Optional.of(expectedVacation1));

	vacationService.delete(1);

	verify(vacationDao).delete(1);
    }
}