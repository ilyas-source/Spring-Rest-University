package ua.com.foxminded.university.service;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.expectedTeacher1;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.expectedTeachers;

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
import ua.com.foxminded.university.model.Teacher;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    private static final Map<String, Integer> vacationDays = Map.ofEntries(
	    entry("BACHELOR", 10),
	    entry("DOCTOR", 20),
	    entry("MASTER", 30));

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
	when(vacationService.getDaysDuration(expectedTeacher1.getVacations().get(0))).thenReturn(20L);
	when(vacationService.getDaysDuration(expectedTeacher1.getVacations().get(1))).thenReturn(20L);

	teacherService.create(expectedTeacher1);

	verify(teacherDao, never()).create(expectedTeacher1);
    }

    @Test
    void givenTeacher_onCreate_shouldCallDaoCreate() {
	teacherService.create(expectedTeacher1);

	verify(teacherDao).create(expectedTeacher1);
    }

    @Test
    void givenSuitableTeacher_onUpdate_shouldCallDaoUpdate() {
	teacherService.update(expectedTeacher1);

	verify(teacherDao).update(expectedTeacher1);
    }

    @Test
    void givenIncorrectTeacherId_onDelete_shouldNotCallDaoDelete() {
	teacherService.delete(1);

	verify(teacherDao, never()).delete(1);
    }
}