package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Vacation;
import ua.com.foxminded.university.repository.VacationRepository;

import java.time.LocalDate;
import java.util.*;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.service.VacationServiceTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class VacationServiceTest {

    @Mock
    private VacationRepository vacationRepository;
    @InjectMocks
    private VacationService vacationService;

    @Test
    void onFindAll_shouldReturnCorrectList() {
        when(vacationRepository.findAll()).thenReturn(expectedVacations);

        assertEquals(expectedVacations, vacationService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectVacation() {
        when(vacationRepository.findById(1)).thenReturn(Optional.of(expectedVacation1));
        Optional<Vacation> expected = Optional.of(expectedVacation1);

        Optional<Vacation> actual = vacationService.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenVacation_onCreate_shouldCallCreate() {
        vacationService.create(expectedVacation1);

        verify(vacationRepository).save(expectedVacation1);
    }

    @Test
    void givenVacation_onUpdate_shouldCallUpdate() {
        vacationService.update(expectedVacation1);

        verify(vacationRepository).save(expectedVacation1);
    }

    @Test
    void givenIncorrectVacationId_onDelete_shouldThrowException() {
        String expected = "Vacation id:1 not found, nothing to delete";

        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> vacationService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(vacationRepository, never()).delete(any());
    }

    @Test
    void givenCorrectVacationId_onDelete_shouldCallDelete() {
        when(vacationRepository.findById(1)).thenReturn(Optional.of(expectedVacation1));

        vacationService.delete(1);

        verify(vacationRepository).delete(expectedVacation1);
    }

    @Test
    void givenVacations_onCountDaysByYears_shouldReturnCorrectMap() {
        List<Vacation> vacationsToCount = new ArrayList<>(
                Arrays.asList(expectedVacation1, vacationGoingOverNewYear));

        Map<Integer, Long> expected = new HashMap<>();
        expected.put(2000, 38L);
        expected.put(2001, 9L);

        Map<Integer, Long> actual = vacationService.countDaysByYears(vacationsToCount);

        assertEquals(expected, actual);
    }

    public interface TestData {
        Vacation vacationToCreate = new Vacation(5, LocalDate.of(2020, 6, 1), LocalDate.of(2020, 7, 1));

        Vacation expectedVacation1 = new Vacation(1, LocalDate.of(2000, 1, 1), LocalDate.of(2000, 2, 1));
        Vacation expectedVacation2 = new Vacation(2, LocalDate.of(2000, 5, 1), LocalDate.of(2000, 6, 1));
        Vacation expectedVacation3 = new Vacation(3, LocalDate.of(2000, 1, 15), LocalDate.of(2000, 2, 15));
        Vacation expectedVacation4 = new Vacation(4, LocalDate.of(2000, 6, 1), LocalDate.of(2000, 7, 1));
        List<Vacation> expectedVacations = new ArrayList<>(
                Arrays.asList(expectedVacation1, expectedVacation2, expectedVacation3, expectedVacation4));

        Vacation vacationGoingOverNewYear = new Vacation(LocalDate.of(2000, 12, 25), LocalDate.of(2001, 1, 10));

        Map<Integer, Long> daysByYearsMap = Map.ofEntries(entry(2000, 20L));
    }
}