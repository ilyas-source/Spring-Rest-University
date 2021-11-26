package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.UniversityProperties;
import ua.com.foxminded.university.exception.*;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.repository.LectureRepository;
import ua.com.foxminded.university.repository.TeacherRepository;

import java.time.LocalDate;
import java.util.*;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.service.LectureServiceTest.TestData.expectedLectures;
import static ua.com.foxminded.university.service.StudentServiceTest.TestData.expectedAddress3;
import static ua.com.foxminded.university.service.SubjectServiceTest.TestData.*;
import static ua.com.foxminded.university.service.TeacherServiceTest.TestData.*;
import static ua.com.foxminded.university.service.TimeslotServiceTest.TestData.expectedTimeslot1;
import static ua.com.foxminded.university.service.VacationServiceTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    private static final Map<Degree, Integer> vacationDaysMap = Map.ofEntries(
            entry(Degree.BACHELOR, 10),
            entry(Degree.DOCTOR, 20),
            entry(Degree.MASTER, 30));

    private static final Map<Degree, Integer> vacationDays = new EnumMap<>(vacationDaysMap);

    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private LectureRepository lectureRepository;
    @Mock
    private VacationService vacationService;
    @Mock
    private UniversityProperties universityProperties;
    @InjectMocks
    private TeacherService teacherService;

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectTeacher() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(expectedTeacher1));
        Optional<Teacher> expected = Optional.of(expectedTeacher1);

        Optional<Teacher> actual = teacherService.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherWithTooLongVacations_onCreate_shouldThrowException() {
        when(universityProperties.getVacationDays()).thenReturn(vacationDays);
        String expected = "Teacher has maximum 20 vacation days per year, can't assign 40 days";
        Map<Integer, Long> daysByYearsMap = new HashMap<>();
        daysByYearsMap.put(2000, 40L);
        daysByYearsMap.put(2001, 20L);
        when(vacationService.countDaysByYears(expectedTeacher1.getVacations())).thenReturn(daysByYearsMap);

        Throwable thrown = assertThrows(VacationInsufficientDaysException.class,
                () -> teacherService.create(expectedTeacher1));

        assertEquals(expected, thrown.getMessage());
        verify(teacherRepository, never()).save(expectedTeacher1);
    }

    @Test
    void givenSuitableTeacher_onCreate_shouldCallRepositoryCreate() {
        when(universityProperties.getVacationDays()).thenReturn(vacationDays);
        when(vacationService.countDaysByYears(expectedTeacher1.getVacations())).thenReturn(daysByYearsMap);

        teacherService.create(expectedTeacher1);

        verify(teacherRepository).save(expectedTeacher1);
    }

    @Test
    void givenSuitableTeacher_onUpdate_shouldCallRepositoryUpdate() {
        when(universityProperties.getVacationDays()).thenReturn(vacationDays);
        when(vacationService.countDaysByYears(expectedTeacher1.getVacations())).thenReturn(daysByYearsMap);

        teacherService.update(expectedTeacher1);

        verify(teacherRepository).save(expectedTeacher1);
    }

    @Test
    void givenIncorrectTeacherId_onDelete_shouldThrowException() {
        String expected = "Can't find teacher by id 1";

        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> teacherService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(teacherRepository, never()).delete(any());
    }

    @Test
    void givenTeacherWithLectures_onDelete_shouldThrowException() {
        String expected = "Teacher Adam Smith has scheduled lecture(s), can't delete";
        when(lectureRepository.findByTeacher(expectedTeacher1)).thenReturn(expectedLectures);
        when(teacherRepository.findById(1)).thenReturn(Optional.of(expectedTeacher1));

        Throwable thrown = assertThrows(TeacherBusyException.class,
                () -> teacherService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(teacherRepository, never()).delete(expectedTeacher1);
    }

    @Test
    void givenTeacherWithNoLecturesId_onDelete_shouldCallRepositoryDelete() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(expectedTeacher1));
        when(lectureRepository.findByTeacher(expectedTeacher1)).thenReturn(new ArrayList<>());

        teacherService.delete(1);

        verify(teacherRepository).delete(expectedTeacher1);
    }

    @Test
    void givenTeacherWithExistingNameAndEmail_onCreate_shouldThrowException() {
        String expected = "Teacher Adam Smith with email adam@smith.com already exists, can't create duplicate";
        when(teacherRepository.findByFirstNameAndLastNameAndEmail(
                "Adam", "Smith", "adam@smith.com")).thenReturn(Optional.of(expectedTeacher2));

        Throwable thrown = assertThrows(EntityNotUniqueException.class,
                () -> teacherService.create(expectedTeacher1));

        assertEquals(expected, thrown.getMessage());
        verify(teacherRepository, never()).save(expectedTeacher1);
    }

    @Test
    void givenTeacherThatCannotTeachRequiredSubjects_onUpdate_shouldThrowException() {
        String expected = "Updated teacher Test Teacher can't teach scheduled lecture(s)";
        when(lectureRepository.findByTeacher(teacherToCreate)).thenReturn(expectedLectures);

        Throwable thrown = assertThrows(TeacherCannotTeachSubject.class,
                () -> teacherService.update(teacherToCreate));

        assertEquals(expected, thrown.getMessage());
        verify(teacherRepository, never()).save(teacherToCreate);
    }

    public interface TestData {
        Set<Subject> testSubjects = new HashSet<>(List.of(subjectToUpdate));
        List<Vacation> vacationsToCreate = new ArrayList<>(List.of(vacationToCreate));

        List<Vacation> expectedVacations1 = new ArrayList<>(Arrays.asList(expectedVacation1, expectedVacation2));
        List<Vacation> expectedVacations2 = new ArrayList<>(Arrays.asList(expectedVacation3, expectedVacation4));
        Set<Subject> expectedSubjects1 = new HashSet<>(Arrays.asList(expectedSubject1, expectedSubject2));
        Set<Subject> expectedSubjects2 = new HashSet<>(Arrays.asList(expectedSubject3, expectedSubject4));

        Teacher teacherToCreate = Teacher.builder().firstName("Test").lastName("Teacher").id(3)
                .gender(Gender.MALE).degree(Degree.DOCTOR).subjects(testSubjects)
                .email("test@mail").phoneNumber("phone").address(expectedAddress3)
                .vacations(vacationsToCreate).build();

        Address expectedAddress1 = Address.builder().country("UK").id(1).postalCode("12345").region("City-Of-Edinburgh")
                .city("Edinburgh").streetAddress("Panmure House").build();
        Address expectedAddress2 = Address.builder().country("Poland").id(2).postalCode("54321").region(
                        "Central region")
                .city("Warsaw").streetAddress("Urszuli Ledochowskiej 3").build();

        Address addressToUpdate = Address.builder().country("test").id(2).postalCode("test").region("test")
                .city("test").streetAddress("test").build();

        Teacher expectedTeacher1 = Teacher.builder().firstName("Adam").lastName("Smith").id(1)
                .gender(Gender.MALE).degree(Degree.DOCTOR).subjects(expectedSubjects1)
                .email("adam@smith.com").phoneNumber("+223322").address(expectedAddress1)
                .vacations(expectedVacations1).build();
        Teacher expectedTeacher2 = Teacher.builder().firstName("Marie").lastName("Curie").id(2)
                .gender(Gender.FEMALE).degree(Degree.MASTER).subjects(expectedSubjects2)
                .email("marie@curie.com").phoneNumber("+322223").address(expectedAddress2)
                .vacations(expectedVacations2).build();
        List<Teacher> expectedTeachers = new ArrayList<>(Arrays.asList(expectedTeacher1, expectedTeacher2));

        Lecture lectureToReplaceTeacher = Lecture.builder().date(LocalDate.of(2021, 1, 1)).
                subject(expectedSubject3).timeslot(expectedTimeslot1).teacher(expectedTeacher2).build();
    }
}