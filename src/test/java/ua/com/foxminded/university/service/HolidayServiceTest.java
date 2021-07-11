package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.HolidayDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.jdbc.JdbcHolidayDao;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private JdbcHolidayDao holidayDao;
    @InjectMocks
    private HolidayService holidayService;

    @Test
    void onFindAll_shouldReturnAllHolidays() {
	when(holidayDao.findAll()).thenReturn(expectedHolidays);

	assertEquals(expectedHolidays, holidayService.findAll());
    }

    @Test
    void givenCorrectId_onFindById_shouldReturnOptionalWithCorrectHoliday() {
	when(holidayDao.findById(1)).thenReturn(Optional.of(expectedHoliday1));

	assertEquals(Optional.of(expectedHoliday1), holidayService.findById(1));
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
    void givenHoliday_onDelete_shouldCallDaoDelete() {
	holidayService.delete(1);

	verify(holidayDao).delete(1);
    }
}