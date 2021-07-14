package ua.com.foxminded.university.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.expectedTimeslot1;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.timeslotToCreate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import ua.com.foxminded.university.dao.TimeslotDao;

@ExtendWith(MockitoExtension.class)
class TimeslotServiceTest {

    private static final int MINIMUM_TIMESLOT_LENGTH = 30;
    private static final int MINIMUM_BREAK_LENGTH = 15;

    @BeforeEach
    void init() {
	ReflectionTestUtils.setField(timeslotService, "minimumTimeslotLength", MINIMUM_TIMESLOT_LENGTH);
	ReflectionTestUtils.setField(timeslotService, "minimumBreakLength", MINIMUM_BREAK_LENGTH);
    }

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
    void givenTimeslot_onCreate_shouldCallDaoCreate() {
	timeslotService.create(expectedTimeslot1);

	verify(timeslotDao).create(expectedTimeslot1);
    }

    @Test
    void givenTimeslot_onUpdate_shouldCallDaoUpdate() {
	timeslotService.update(expectedTimeslot1);

	verify(timeslotDao).update(expectedTimeslot1);
    }

    @Test
    void givenIncorrectTimeslotId_onDelete_shouldNotCallDaoDelete() {
	timeslotService.delete(1);

	verify(timeslotDao, never()).delete(1);
    }

    @Test
    void givenShortTimeslot_onCreate_shouldNotCallCreate() {
	timeslotService.create(timeslotToCreate);

	verify(timeslotDao, never()).create(timeslotToCreate);
    }
}