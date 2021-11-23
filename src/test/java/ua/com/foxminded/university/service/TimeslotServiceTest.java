package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.TimeslotDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.TimeslotInUseException;
import ua.com.foxminded.university.exception.TimeslotTooShortException;
import ua.com.foxminded.university.exception.TimeslotsIntersectionException;
import ua.com.foxminded.university.model.Timeslot;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.service.LectureServiceTest.TestData.expectedLectures;
import static ua.com.foxminded.university.service.TimeslotServiceTest.TestData.*;

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
    @Mock
    private LectureDao lectureDao;
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
    void givenIncorrectTimeslotId_onDelete_shouldThrowException() {
        String expected = "Can't find timeslot by id 1";

        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> timeslotService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(timeslotDao, never()).delete(any());
    }

    @Test
    void givenShortTimeslot_onCreate_shouldThrowException() {
        String expected = "Minimum timeslot length 30 min, but was 15 min, can't create timeslot";

        Throwable thrown = assertThrows(TimeslotTooShortException.class,
                () -> timeslotService.create(timeslotToCreate));

        assertEquals(expected, thrown.getMessage());
        verify(timeslotDao, never()).create(timeslotToCreate);
    }

    @Test
    void givenFreeTimeslot_onDelete_shouldCallDaoDelete() {
        when(timeslotDao.findById(1)).thenReturn(Optional.of(expectedTimeslot1));
        when(lectureDao.findByTimeslot(expectedTimeslot1)).thenReturn(new ArrayList<>());

        timeslotService.delete(1);

        verify(timeslotDao).delete(expectedTimeslot1);
    }

    @Test
    void givenTimeslotWithLectures_onDelete_shouldThrowException() {
        String expected = "Timeslot has sheduled lectures, can't delete";

        when(timeslotDao.findById(1)).thenReturn(Optional.of(expectedTimeslot1));
        when(lectureDao.findByTimeslot(expectedTimeslot1)).thenReturn(expectedLectures);

        Throwable thrown = assertThrows(TimeslotInUseException.class,
                () -> timeslotService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(timeslotDao, never()).delete(expectedTimeslot1);
    }

    @Test
    void givenIntersectingTimeslot_onCreate_shouldThrowException() {
        String expected = "New timeslot has intersections with existing timetable, can't create/update";
        when(timeslotDao.countIntersectingTimeslots(timeslotWithBreaks)).thenReturn(1);

        Throwable thrown = assertThrows(TimeslotsIntersectionException.class,
                () -> timeslotService.create(expectedTimeslot1));

        assertEquals(expected, thrown.getMessage());
        verify(timeslotDao, never()).create(expectedTimeslot1);
    }

        public interface TestData {
        Timeslot timeslotToCreate = new Timeslot(4, LocalTime.of(12, 0), LocalTime.of(12, 15));
        Timeslot timeslotToUpdate = new Timeslot(2, LocalTime.of(12, 0), LocalTime.of(12, 15));

        Timeslot expectedTimeslot1 = new Timeslot(1, LocalTime.of(9, 0), LocalTime.of(9, 45));
        Timeslot expectedTimeslot2 = new Timeslot(2, LocalTime.of(10, 0), LocalTime.of(10, 45));
        Timeslot expectedTimeslot3 = new Timeslot(3, LocalTime.of(11, 0), LocalTime.of(11, 45));

        Timeslot intersectingTimeslot = new Timeslot(LocalTime.of(10, 30), LocalTime.of(11, 15));

        List<Timeslot> expectedTimeslots = new ArrayList<>(
                Arrays.asList(expectedTimeslot1, expectedTimeslot2, expectedTimeslot3));

        Timeslot timeslotWithBreaks = new Timeslot(LocalTime.of(8, 45), LocalTime.of(10, 0));
    }
}