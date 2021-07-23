package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.VacationDaoTest.TestData.expectedVacation1;
import static ua.com.foxminded.university.dao.VacationDaoTest.TestData.expectedVacations;
import static ua.com.foxminded.university.dao.VacationDaoTest.TestData.vacationGoingOverNewYear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.VacationsIntersectionException;
import ua.com.foxminded.university.model.Vacation;

@ExtendWith(MockitoExtension.class)
class VacationServiceTest {

    @Mock
    private VacationDao vacationDao;
    @InjectMocks
    private VacationService vacationService;

    @Test
    void onFindAll_shouldReturnCorrectList() {
	when(vacationDao.findAll()).thenReturn(expectedVacations);

	assertEquals(expectedVacations, vacationService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectVacation() {
	when(vacationDao.findById(1)).thenReturn(Optional.of(expectedVacation1));
	Optional<Vacation> expected = Optional.of(expectedVacation1);

	Optional<Vacation> actual = vacationService.findById(1);

	assertEquals(expected, actual);
    }

    @Test
    void givenVacation_onCreate_shouldCallCreate() {
	vacationService.create(expectedVacation1);

	verify(vacationDao).create(expectedVacation1);
    }

    @Test
    void givenIntersectingVacation_onCreate_shoultThrowException() {
	String expected = "New vacation has intersections with existing vacations, can't create/update";
	when(vacationDao.countIntersectingVacations(expectedVacation1)).thenReturn(1);

	Throwable thrown = assertThrows(VacationsIntersectionException.class,
		() -> vacationService.create(expectedVacation1));

	assertEquals(expected, thrown.getMessage());
	verify(vacationDao, never()).create(expectedVacation1);
    }

    @Test
    void givenVacation_onUpdate_shouldCallUpdate() {
	vacationService.update(expectedVacation1);

	verify(vacationDao).update(expectedVacation1);
    }

    @Test
    void givenIncorrectVacationId_onDelete_shouldThrowException() {
	String expected = "Vacation with id:1 not found, nothing to delete";

	Throwable thrown = assertThrows(EntityNotFoundException.class,
		() -> vacationService.delete(1));

	assertEquals(expected, thrown.getMessage());
	verify(vacationDao, never()).delete(1);
    }

    @Test
    void givenCorrectVacationId_onDelete_shouldCallDelete() {
	when(vacationDao.findById(1)).thenReturn(Optional.of(expectedVacation1));

	vacationService.delete(1);

	verify(vacationDao).delete(1);
    }

    @Test
    void givenVacations_onCountDaysByYears_shouldReturnCorrectMap() {
	List<Vacation> vacationsToCount = new ArrayList<>(
		Arrays.asList(expectedVacation1, vacationGoingOverNewYear));

	Map<Integer, Long> expected = new HashMap<>();
	expected.put(2000, 38L);
	expected.put(2001, 9L);

	Map<Integer, Long> actual = vacationService.countDaysByYears(vacationsToCount);
	System.out.println(actual);

	assertEquals(expected, actual);
    }
}