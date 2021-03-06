package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.repository.HolidayRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.service.HolidayServiceTest.TestData.expectedHoliday1;
import static ua.com.foxminded.university.service.HolidayServiceTest.TestData.expectedHolidays;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private HolidayRepository holidayRepository;
    @InjectMocks
    private HolidayService holidayService;

    @Test
    void onFindAll_shouldReturnCorrectList() {
        when(holidayRepository.findAll()).thenReturn(expectedHolidays);

        assertEquals(expectedHolidays, holidayService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectHoliday() {
        when(holidayRepository.findById(1)).thenReturn(Optional.of(expectedHoliday1));
        Optional<Holiday> expected = Optional.of(expectedHoliday1);

        Optional<Holiday> actual = holidayService.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenHoliday_onCreate_shouldCallRepositoryCreate() {
        holidayService.create(expectedHoliday1);

        verify(holidayRepository).save(expectedHoliday1);
    }

    @Test
    void givenHoliday_onUpdate_shouldCallRepositoryUpdate() {
        holidayService.update(expectedHoliday1);

        verify(holidayRepository).save(expectedHoliday1);
    }

    @Test
    void givenIncorrectHolidayId_onDelete_shouldThrowException() {
        String expected = "Holiday id:1 not found, nothing to delete";

        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> holidayService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(holidayRepository, never()).delete(any());
    }

    @Test
    void givenExistingId_onDelete_shouldCallRepositoryDelete() {
        when(holidayRepository.findById(1)).thenReturn(Optional.of(expectedHoliday1));

        holidayService.delete(1);

        verify(holidayRepository).delete(expectedHoliday1);
    }

    public interface TestData {
        Holiday expectedHoliday1 = new Holiday(1, LocalDate.of(2000, 12, 25), "Christmas");
        Holiday expectedHoliday2 = new Holiday(2, LocalDate.of(2000, 10, 30), "Halloween");
        Holiday expectedHoliday3 = new Holiday(3, LocalDate.of(2000, 3, 8), "International womens day");

        List<Holiday> expectedHolidays = new ArrayList<>(
                Arrays.asList(expectedHoliday1, expectedHoliday2, expectedHoliday3));
    }
}