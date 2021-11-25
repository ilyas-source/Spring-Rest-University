package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.repository.GroupRepositoryTest.TestData.expectedGroup1;
import static ua.com.foxminded.university.repository.GroupRepositoryTest.TestData.expectedGroup2;
import static ua.com.foxminded.university.repository.StudentRepositoryTest.TestData.expectedStudent1;
import static ua.com.foxminded.university.repository.StudentRepositoryTest.TestData.expectedStudent3;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Test
    void givenGroup_onCountStudentsInGroup_shouldReturnCorrectNumber() {
        int expected = 2;

        int actual = studentRepository.countByGroup(expectedGroup1);

        assertEquals(expected, actual);
    }

    @Test
    void givenGroup_onFindByGroup_shouldReturnCorrectListOfStudents() {
        List<Student> expected = new ArrayList<>(Arrays.asList(expectedStudent1, expectedStudent3));

        List<Student> actual = studentRepository.findByGroup(expectedGroup1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNameAndBirthdate_onFindByNameAndBirthdate_shouldReturnOptionalWithCorrectStudent() {
        Optional<Student> expected = Optional.of(expectedStudent1);

        Optional<Student> actual = studentRepository.findByFirstNameAndLastNameAndBirthDate("Ivan", "Petrov", LocalDate.of(1980, 11, 1));

        assertEquals(expected, actual);
    }

    @Test
    void givenWrongData_onFindByNameAndBirthdate_shouldReturnOptionalEmpty() {
        Optional<Student> actual = studentRepository.findByFirstNameAndLastNameAndBirthDate("Ivan1", "Petrov", LocalDate.of(1980, 11, 1));

        assertEquals(Optional.empty(), actual);
    }

    @Test
    void givenSubstring_onFindBySubstring_thenReturnCorrectListOfStudents() {
        var expected = new ArrayList<>(List.of(expectedStudent1));

        var actual = studentRepository.findByFirstNameContainingOrLastNameContainingAllIgnoreCase("ivAn", "ivan");

        assertEquals(expected, actual);
    }

    interface TestData {
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
    }
}
