package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.foxminded.university.model.Holiday;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.repository.HolidayRepositoryTest.TestData.expectedHoliday1;

@DataJpaTest
public class HolidayRepositoryTest {

    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    void givenDate_onFindByDate_shouldReturnListWithCorrectHolidays() {
        List<Holiday> expected = new ArrayList<>(List.of(expectedHoliday1));

        List<Holiday> actual = holidayRepository.findByDate(expectedHoliday1.getDate());

        assertEquals(expected, actual);
    }

    interface TestData {
        Holiday expectedHoliday1 = new Holiday(1, LocalDate.of(2000, 12, 25), "Christmas");
    }
}
