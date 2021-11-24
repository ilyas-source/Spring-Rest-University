package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.foxminded.university.model.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.repository.SubjectRepositoryTest.TestData.*;
import static ua.com.foxminded.university.repository.TeacherRepositoryTest.TestData.expectedTeacher1;
import static ua.com.foxminded.university.repository.TeacherRepositoryTest.TestData.replacementTeacher;
import static ua.com.foxminded.university.repository.VacationRepositoryTest.TestData.*;

@DataJpaTest
public class TeacherRepositoryTest {

    @Autowired
    TeacherRepository teacherRepository;

    @Test
    void testTeacherIsCorrectInTestDatabase() {
        System.out.println("*************************");
        System.out.println(teacherRepository.getById(1));
        System.out.println(teacherRepository.getById(2));
        System.out.println(teacherRepository.getById(3));
        System.out.println(teacherRepository.getById(4));
        System.out.println(teacherRepository.findAll().size());
        System.out.println("*************************");
    }

    @Test
    void givenString_onFindBySubstring_shouldReturnCorrectListOfTeachers() {
        var expected = new ArrayList<>(List.of(expectedTeacher1));

        var actual = teacherRepository.findBySubstring("adam s");

        assertEquals(expected, actual);
    }

    @Test
    void givenNameAndEmail_onFindByNameAndEmail_shouldReturnOptionalWithCorrectTeacher() {
        var actual = teacherRepository.findByFirstNameAndLastNameAndEmail(
                "Adam", "Smith", "adam@smith.com");

        assertEquals(Optional.of(expectedTeacher1), actual);
    }

    @Test
    void givenSubjectIdAndTeacherId_onGetReplacementCandidates_shouldReturnCorrectListOfTeachers() {
        teacherRepository.save(replacementTeacher);

        var expected = new ArrayList<>(List.of(replacementTeacher));

        var actual = teacherRepository.getReplacementCandidates(3, 2);

        assertEquals(expected, actual);
        teacherRepository.delete(replacementTeacher);
    }

    interface TestData {
        Address expectedAddress1 = Address.builder().country("UK").id(1).postalCode("12345").region("City-Of-Edinburgh")
                .city("Edinburgh").streetAddress("Panmure House").build();
        Address expectedAddress2 = Address.builder().country("Poland").id(2).postalCode("54321").region(
                        "Central region")
                .city("Warsaw").streetAddress("Urszuli Ledochowskiej 3").build();

        List<Vacation> expectedVacations1 = new ArrayList<>(Arrays.asList(expectedVacation1, expectedVacation2));
        List<Vacation> expectedVacations2 = new ArrayList<>(Arrays.asList(expectedVacation3, expectedVacation4));

        Set<Subject> expectedSubjects1 = new HashSet<>(Arrays.asList(expectedSubject1, expectedSubject2));
        Set<Subject> expectedSubjects2 = new HashSet<>(Arrays.asList(expectedSubject3, expectedSubject4));

        Teacher expectedTeacher1 = Teacher.builder().firstName("Adam").lastName("Smith").id(1)
                .gender(Gender.MALE).degree(Degree.DOCTOR).subjects(expectedSubjects1)
                .email("adam@smith.com").phoneNumber("+223322").address(expectedAddress1)
                .vacations(expectedVacations1).build();
        Teacher expectedTeacher2 = Teacher.builder().firstName("Marie").lastName("Curie").id(2)
                .gender(Gender.FEMALE).degree(Degree.MASTER).subjects(expectedSubjects2)
                .email("marie@curie.com").phoneNumber("+322223").address(expectedAddress2)
                .vacations(expectedVacations2).build();
        Teacher replacementTeacher = Teacher.builder().firstName("Replacement").lastName("Teacher").id(3)
                .gender(Gender.FEMALE).degree(Degree.MASTER).subjects(expectedSubjects2)
                .email("new@teacher.com").phoneNumber("+322223").address(expectedAddress2)
                .vacations(expectedVacations2).build();
    }

}
