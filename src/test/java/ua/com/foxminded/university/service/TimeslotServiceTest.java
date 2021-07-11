package ua.com.foxminded.university.service;

import static org.mockito.Mockito.verify;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.expectedTimeslot1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.TimeslotDao;

@ExtendWith(MockitoExtension.class)
class TimeslotServiceTest {

    @Mock
    private TimeslotDao timeslotDao;
    @InjectMocks
    private TimeslotService timeslotService;

    @Test
    void onFindAll_shouldCallDaoFindAll() {
	timeslotService.findAll();

	verify(timeslotDao).findAll();
    }

    @Test
    void givenId_onFindById_shouldCallDaoFindById() {
	timeslotService.findById(1);

	verify(timeslotDao).findById(1);
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