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

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLectures;
import static ua.com.foxminded.university.dao.HibernateTimeslotDaoTest.TestData.*;

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
    //    verify(timeslotDao, never()).delete(1);
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

    //    verify(timeslotDao).delete(1);
    }

    @Test
    void givenTimeslotWithLectures_onDelete_shouldThrowException() {
        String expected = "Timeslot has sheduled lectures, can't delete";

        when(timeslotDao.findById(1)).thenReturn(Optional.of(expectedTimeslot1));
        when(lectureDao.findByTimeslot(expectedTimeslot1)).thenReturn(expectedLectures);

        Throwable thrown = assertThrows(TimeslotInUseException.class,
                () -> timeslotService.delete(1));

        assertEquals(expected, thrown.getMessage());
     //   verify(timeslotDao, never()).delete(1);
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
}