package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.foxminded.university.model.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.dao.HibernateGroupDaoTest.TestData.expectedGroup1;
import static ua.com.foxminded.university.dao.HibernateStudentDaoTest.TestData.expectedStudent1;
import static ua.com.foxminded.university.dao.HibernateStudentDaoTest.TestData.expectedStudent3;

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

        var actual = studentRepository.findBySubstring("ivAn pe");

        assertEquals(expected, actual);
    }
}
