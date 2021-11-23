package ua.com.foxminded.university.repository;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.foxminded.university.model.Timeslot;

import java.time.LocalTime;

@DataJpaTest
public class TimeslotRepositoryTest {

    interface TestData {
        Timeslot expectedTimeslot1 = new Timeslot(1, LocalTime.of(9, 0), LocalTime.of(9, 45));
        Timeslot expectedTimeslot2 = new Timeslot(2, LocalTime.of(10, 0), LocalTime.of(10, 45));
    }
}
