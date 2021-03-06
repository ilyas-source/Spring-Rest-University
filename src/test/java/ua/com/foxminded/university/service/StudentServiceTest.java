package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.UniversityProperties;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.exception.GroupOverflowException;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.repository.StudentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.service.GroupServiceTest.TestData.expectedGroup1;
import static ua.com.foxminded.university.service.GroupServiceTest.TestData.expectedGroup2;
import static ua.com.foxminded.university.service.StudentServiceTest.TestData.expectedStudent1;
import static ua.com.foxminded.university.service.StudentServiceTest.TestData.expectedStudent2;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    private static final int MAX_STUDENTS_IN_GROUP = 30;

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private UniversityProperties universityProperties;
    @InjectMocks
    private StudentService studentService;

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectStudent() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(expectedStudent1));
        Optional<Student> expected = Optional.of(expectedStudent1);

        Optional<Student> actual = studentService.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenUniqueStudent_onCreate_shouldCallRepositoryCreate() {
        when(universityProperties.getMaxStudents()).thenReturn(MAX_STUDENTS_IN_GROUP);
        studentService.create(expectedStudent1);

        verify(studentRepository).save(expectedStudent1);
    }

    @Test
    void givenExcessiveStudent_onCreate_shouldThrowException() {
        when(universityProperties.getMaxStudents()).thenReturn(MAX_STUDENTS_IN_GROUP);
        String expected = "Group limit of 30 students reached, can't add more";
        when(studentRepository.countByGroup(expectedStudent1.getGroup())).thenReturn(30);

        Throwable thrown = assertThrows(GroupOverflowException.class,
                () -> studentService.create(expectedStudent1));

        assertEquals(expected, thrown.getMessage());
        verify(studentRepository, never()).save(expectedStudent1);
    }

    @Test
    void givenNonUniqueStudent_onCreate_shouldThrowException() {
        String expected = "Student Ivan Petrov, born 1980-11-01 already exists, can't create duplicate";
        when(studentRepository.findByFirstNameAndLastNameAndBirthDate(expectedStudent1.getFirstName(), expectedStudent1.getLastName(),
                expectedStudent1.getBirthDate())).thenReturn(Optional.of(expectedStudent2));

        Throwable thrown = assertThrows(EntityNotUniqueException.class,
                () -> studentService.create(expectedStudent1));

        assertEquals(expected, thrown.getMessage());
        verify(studentRepository, never()).save(expectedStudent1);
    }

    @Test
    void givenStudent_onUpdate_shouldCallRepositoryUpdate() {
        studentService.update(expectedStudent1);

        verify(studentRepository).save(expectedStudent1);
    }

    @Test
    void givenExistingStudentId_onDelete_shouldCallRepositoryDelete() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(expectedStudent1));

        studentService.delete(1);

        verify(studentRepository).delete(expectedStudent1);
    }

    @Test
    void givenIncorrectStudentId_onDelete_shouldThrowException() {
        String expected = "Student with id:1 not found, nothing to delete";

        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> studentService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(studentRepository, never()).delete(any());
    }

    public interface TestData {
        Address expectedAddress3 = Address.builder().country("Russia").id(3).postalCode("450080").region(
                        "Permskiy kray")
                .city("Perm").streetAddress("Lenina 5").build();
        Address expectedAddress4 = Address.builder().country("USA").id(4).postalCode("90210").region("California")
                .city("LA").streetAddress("Grove St. 15").build();
        Address expectedAddress5 = Address.builder().country("France").id(5).postalCode("21012").region("Central")
                .city("Paris").streetAddress("Rue 15").build();
        Address expectedAddress6 = Address.builder().country("China").id(6).postalCode("20121").region("Guangdung")
                .city("Beijin").streetAddress("Main St. 125").build();

        Student expectedStudent1 = Student.builder().firstName("Ivan").lastName("Petrov")
                .id(1).gender(Gender.MALE).birthDate(LocalDate.of(1980, 11, 1))
                .email("qwe@rty.com").phone("123123123").address(expectedAddress3)
                .group(expectedGroup1).build();
        Student expectedStudent2 = Student.builder().firstName("John").lastName("Doe")
                .id(2).gender(Gender.MALE).birthDate(LocalDate.of(1981, 11, 1))
                .email("qwe@qwe.com").phone("1231223").address(expectedAddress4)
                .group(expectedGroup2).build();
        Student expectedStudent3 = Student.builder().firstName("Janna").lastName("DArk")
                .id(3).gender(Gender.FEMALE).birthDate(LocalDate.of(1881, 11, 1))
                .email("qwe@no.fr").phone("1231223").address(expectedAddress5)
                .group(expectedGroup1).build();
        Student expectedStudent4 = Student.builder().firstName("Mao").lastName("Zedun")
                .id(4).gender(Gender.MALE).birthDate(LocalDate.of(1921, 9, 14))
                .email("qwe@no.cn").phone("1145223").address(expectedAddress6)
                .group(expectedGroup2).build();

        List<Student> expectedStudents = new ArrayList<>(
                Arrays.asList(expectedStudent1, expectedStudent2, expectedStudent3, expectedStudent4));
    }
}