package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Holiday;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.dao.HolidayDaoTest.TestData.expectedHoliday1;
import static ua.com.foxminded.university.dao.HolidayDaoTest.TestData.expectedHolidays;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private HolidayDao holidayDao;
    @InjectMocks
    private HolidayService holidayService;

    @Test
    void onFindAll_shouldReturnCorrectList() {
        when(holidayDao.findAll()).thenReturn(expectedHolidays);

        assertEquals(expectedHolidays, holidayService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectHoliday() {
        when(holidayDao.findById(1)).thenReturn(Optional.of(expectedHoliday1));
        Optional<Holiday> expected = Optional.of(expectedHoliday1);

        Optional<Holiday> actual = holidayService.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenHoliday_onCreate_shouldCallDaoCreate() {
        holidayService.create(expectedHoliday1);

        verify(holidayDao).create(expectedHoliday1);
    }

    @Test
    void givenHoliday_onUpdate_shouldCallDaoUpdate() {
        holidayService.update(expectedHoliday1);

        verify(holidayDao).update(expectedHoliday1);
    }

    @Test
    void givenIncorrectHolidayId_onDelete_shouldThrowException() {
        String expected = "Holiday id:1 not found, nothing to delete";

        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> holidayService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(holidayDao, never()).delete(1);
    }

    @Test
    void givenExistingId_onDelete_shouldCallDaoDelete() {
        when(holidayDao.findById(1)).thenReturn(Optional.of(expectedHoliday1));

        holidayService.delete(1);

        verify(holidayDao).delete(1);
    }
}