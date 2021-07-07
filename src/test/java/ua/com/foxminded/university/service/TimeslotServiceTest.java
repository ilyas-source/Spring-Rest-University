package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.jdbc.JdbcTimeslotDao;

@ExtendWith(MockitoExtension.class)
class TimeslotServiceTest {

    @Mock
    private JdbcTimeslotDao timeslotDao;
    @InjectMocks
    private TimeslotService timeslotService;

    @Test
    void onFindAll_shouldReturnAllTimeslots() {
	when(timeslotDao.findAll()).thenReturn(expectedTimeslots);

	assertEquals(expectedTimeslots, timeslotService.findAll());
    }

    @Test
    void givenCorrectId_onFindById_shouldReturnOptionalWithCorrectTimeslot() {
	when(timeslotDao.findById(1)).thenReturn(Optional.of(expectedTimeslot1));

	assertEquals(Optional.of(expectedTimeslot1), timeslotService.findById(1));
    }

    @Test
    void givenTimeslot_onCreate_shouldCallCreate() {
	timeslotService.create(expectedTimeslot1);

	verify(timeslotDao).create(expectedTimeslot1);
    }

    @Test
    void givenTimeslot_onUpdate_shouldCallUpdate() {
	timeslotService.update(expectedTimeslot1);

	verify(timeslotDao).update(expectedTimeslot1);
    }

    @Test
    void givenTimeslot_onDelete_shouldCallDelete() {
	timeslotService.delete(1);

	verify(timeslotDao).delete(1);
    }
}