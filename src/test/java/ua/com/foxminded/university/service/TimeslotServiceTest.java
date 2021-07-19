package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.expectedTimeslot1;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.expectedTimeslots;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.timeslotToCreate;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import ua.com.foxminded.university.dao.TimeslotDao;
import ua.com.foxminded.university.model.Timeslot;

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
    void onFindAll_shouldReturnCorrectList() {
	when(timeslotDao.findAll()).thenReturn(expectedTimeslots);

	assertEquals(expectedTimeslots, timeslotService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectTimeslot() {
	when(timeslotDao.findById(1)).thenReturn(Optional.of(expectedTimeslot1));
	Optional<Timeslot> expected = Optional.of(expectedTimeslot1);

	Optional<Timeslot> actual = timeslotService.findById(1);

	assertEquals(expected, actual);
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