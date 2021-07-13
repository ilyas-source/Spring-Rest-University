package ua.com.foxminded.university.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.expectedTeacher1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.TeacherDao;

@TestPropertySource("classpath:university.properties")
@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    private static final int BACHELOR_VACATION_DAYS = 10;
    private static final int DOCTOR_VACATION_DAYS = 20;
    private static final int MASTER_VACATION_DAYS = 30;

    @BeforeEach
    void init() {
	ReflectionTestUtils.setField(teacherService, "bachelorVacationDays", BACHELOR_VACATION_DAYS);
	ReflectionTestUtils.setField(teacherService, "doctorVacationDays", DOCTOR_VACATION_DAYS);
	ReflectionTestUtils.setField(teacherService, "masterVacationDays", MASTER_VACATION_DAYS);
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
    void onFindAll_shouldCallDaoFindAll() {
	teacherService.findAll();

	verify(teacherDao).findAll();
    }

    @Test
    void givenId_onFindById_shouldCallDaoFindById() {
	teacherService.findById(1);

	verify(teacherDao).findById(1);
    }

    @Test
    void givenTeacherWithTooLongVacations_onCreate_shouldNotCallDaoCreate() {
	when(vacationService.countLength(expectedTeacher1.getVacations().get(0))).thenReturn(20);
	when(vacationService.countLength(expectedTeacher1.getVacations().get(1))).thenReturn(20);

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