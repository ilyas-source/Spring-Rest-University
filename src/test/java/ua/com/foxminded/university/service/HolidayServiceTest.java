package ua.com.foxminded.university.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static ua.com.foxminded.university.dao.HolidayDaoTest.TestData.expectedHoliday1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.HolidayDao;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private HolidayDao holidayDao;
    @InjectMocks
    private HolidayService holidayService;

    @Test
    void onFindAll_shouldCallDaoFindAll() {
	holidayService.findAll();

	verify(holidayDao).findAll();
    }

    @Test
    void givenId_onFindById_shouldCallDaoFindById() {
	holidayService.findById(1);

	verify(holidayDao).findById(1);
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
    void givenIncorrectHolidayId_onDelete_shouldNotCallDaoDelete() {
	holidayService.delete(1);

	verify(holidayDao, never()).delete(1);
    }
}