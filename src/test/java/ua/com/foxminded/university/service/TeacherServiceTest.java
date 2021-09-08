package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.exception.*;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Teacher;

import java.util.*;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.dao.LectureDaoTest.TestData.expectedLectures;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.VacationDaoTest.TestData.daysByYearsMap;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    private static final Map<Degree, Integer> vacationDaysMap = Map.ofEntries(
            entry(Degree.BACHELOR, 10),
            entry(Degree.DOCTOR, 20),
            entry(Degree.MASTER, 30));

    private static final Map<Degree, Integer> vacationDays = new EnumMap<>(vacationDaysMap);

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(teacherService, "vacationDays", vacationDays);
    }

    @Mock
    private TeacherDao teacherDao;
    @Mock
    private LectureDao lectureDao;
    @Mock
    private VacationService vacationService;
    @InjectMocks
    private TeacherService teacherService;

//    @Test
//    void onFindAll_shouldReturnCorrectList() {
//        when(teacherDao.findAll()).thenReturn(expectedTeachers);
//
//        assertEquals(expectedTeachers, teacherService.findAll());
//    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectTeacher() {
        when(teacherDao.findById(1)).thenReturn(Optional.of(expectedTeacher1));
        Optional<Teacher> expected = Optional.of(expectedTeacher1);

        Optional<Teacher> actual = teacherService.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherWithTooLongVacations_onCreate_shouldThrowException() {
        String expected = "Teacher has maximum 20 vacation days per year, can't assign 40 days";
        Map<Integer, Long> daysByYearsMap = new HashMap<>();
        daysByYearsMap.put(2000, 40L);
        daysByYearsMap.put(2001, 20L);
        when(vacationService.countDaysByYears(expectedTeacher1.getVacations())).thenReturn(daysByYearsMap);

        Throwable thrown = assertThrows(VacationInsufficientDaysException.class,
                () -> teacherService.create(expectedTeacher1));

        assertEquals(expected, thrown.getMessage());
        verify(teacherDao, never()).create(expectedTeacher1);
    }

    @Test
    void givenSuitableTeacher_onCreate_shouldCallDaoCreate() {
        when(vacationService.countDaysByYears(expectedTeacher1.getVacations())).thenReturn(daysByYearsMap);

        teacherService.create(expectedTeacher1);

        verify(teacherDao).create(expectedTeacher1);
    }

    @Test
    void givenSuitableTeacher_onUpdate_shouldCallDaoUpdate() {
        when(vacationService.countDaysByYears(expectedTeacher1.getVacations())).thenReturn(daysByYearsMap);

        teacherService.update(expectedTeacher1);

        verify(teacherDao).update(expectedTeacher1);
    }

    @Test
    void givenIncorrectTeacherId_onDelete_shouldThrowException() {
        String expected = "Teacher id:1 not found, nothing to delete";

        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> teacherService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(teacherDao, never()).delete(1);
    }

    @Test
    void givenTeacherWithLectures_onDelete_shouldThrowException() {
        String expected = "Teacher Adam Smith has scheduled lecture(s), can't delete";
        when(lectureDao.findByTeacher(expectedTeacher1)).thenReturn(expectedLectures);
        when(teacherDao.findById(1)).thenReturn(Optional.of(expectedTeacher1));

        Throwable thrown = assertThrows(TeacherBusyException.class,
                () -> teacherService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(teacherDao, never()).delete(1);
    }

    @Test
    void givenTeacherWithNoLecturesId_onDelete_shouldCallDaoDelete() {
        when(teacherDao.findById(1)).thenReturn(Optional.of(expectedTeacher1));
        when(lectureDao.findByTeacher(expectedTeacher1)).thenReturn(new ArrayList<>());

        teacherService.delete(1);

        verify(teacherDao).delete(1);
    }

    @Test
    void givenTeacherWithExistingNameAndEmail_onCreate_shouldThrowException() {
        String expected = "Teacher Adam Smith with email adam@smith.com already exists, can't create duplicate";
        when(teacherDao.findByNameAndEmail("Adam", "Smith", "adam@smith.com")).thenReturn(Optional.of(expectedTeacher2));

        Throwable thrown = assertThrows(EntityNotUniqueException.class,
                () -> teacherService.create(expectedTeacher1));

        assertEquals(expected, thrown.getMessage());
        verify(teacherDao, never()).create(expectedTeacher1);
    }

    @Test
    void givenTeacherThatCannotTeachRequiredSubjects_onUpdate_shouldThrowException() {
        String expected = "Updated teacher Test Teacher can't teach scheduled lecture(s)";
        when(lectureDao.findByTeacher(teacherToCreate)).thenReturn(expectedLectures);

        Throwable thrown = assertThrows(TeacherCannotTeachSubject.class,
                () -> teacherService.update(teacherToCreate));

        assertEquals(expected, thrown.getMessage());
        verify(teacherDao, never()).update(teacherToCreate);
    }
}