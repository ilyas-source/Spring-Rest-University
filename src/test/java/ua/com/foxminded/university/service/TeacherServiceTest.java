package ua.com.foxminded.university.service;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.expectedTeacher1;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.expectedTeachers;
import static ua.com.foxminded.university.dao.VacationDaoTest.TestData.daysByYearsMap;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Teacher;

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

    @Test
    void onFindAll_shouldReturnCorrectList() {
	when(teacherDao.findAll()).thenReturn(expectedTeachers);

	assertEquals(expectedTeachers, teacherService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectTeacher() {
	when(teacherDao.findById(1)).thenReturn(Optional.of(expectedTeacher1));
	Optional<Teacher> expected = Optional.of(expectedTeacher1);

	Optional<Teacher> actual = teacherService.findById(1);

	assertEquals(expected, actual);
    }

    @Test
    void givenTeacherWithTooLongVacations_onCreate_shouldNotCallDaoCreate() {
	Map<Integer, Long> daysByYearsMap = new HashMap<>();
	daysByYearsMap.put(2000, 40L);
	daysByYearsMap.put(2001, 20L);
	when(vacationService.countDaysByYears(expectedTeacher1.getVacations())).thenReturn(daysByYearsMap);

	teacherService.create(expectedTeacher1);

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
    void givenIncorrectTeacherId_onDelete_shouldNotCallDaoDelete() {
	teacherService.delete(1);

	verify(teacherDao, never()).delete(1);
    }
}