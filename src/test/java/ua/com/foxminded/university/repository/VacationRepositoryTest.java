package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.foxminded.university.model.Vacation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.repository.TeacherRepositoryTest.TestData.expectedTeacher1;
import static ua.com.foxminded.university.repository.VacationRepositoryTest.TestData.expectedVacation1;
import static ua.com.foxminded.university.repository.VacationRepositoryTest.TestData.expectedVacation2;

@DataJpaTest
public class VacationRepositoryTest {

    @Autowired
    VacationRepository vacationRepository;

    @Test
    void givenTeacher_onFindByTeacher_shouldReturnCorrectListWithVacations() {
        var expected = new ArrayList<>(Arrays.asList(expectedVacation1, expectedVacation2));

        var actual = vacationRepository.findByTeacher(expectedTeacher1);

        assertEquals(expected, actual);
    }

    interface TestData {
        Vacation expectedVacation1 = new Vacation(1, LocalDate.of(2000, 1, 1), LocalDate.of(2000, 2, 1));
        Vacation expectedVacation2 = new Vacation(2, LocalDate.of(2000, 5, 1), LocalDate.of(2000, 6, 1));
        Vacation expectedVacation3 = new Vacation(3, LocalDate.of(2000, 1, 15), LocalDate.of(2000, 2, 15));
        Vacation expectedVacation4 = new Vacation(4, LocalDate.of(2000, 6, 1), LocalDate.of(2000, 7, 1));
    }
}
