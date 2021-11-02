package ua.com.foxminded.university.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.hibernate.HibernateTeacherDao;
import ua.com.foxminded.university.model.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ua.com.foxminded.university.dao.HibernateAddressDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.SubjectDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.TimeslotDaoTest.TestData.expectedTimeslot1;
import static ua.com.foxminded.university.dao.HibernateVacationDaoTest.TestData.*;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class TeacherDaoTest {

    private static final String TEST_WHERE_CLAUSE = "first_name='Test' AND last_name='Teacher' AND gender='MALE' AND degree='DOCTOR' AND email='test@mail' AND phone='phone'";

    @Autowired
    private HibernateTeacherDao teacherDao;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    void givenNewTeacher_onCreate_shouldCreateTeacher() {
        var actual = hibernateTemplate.get(Teacher.class, 3);
        assertNull(actual);

        teacherDao.create(teacherToCreate);

        actual = hibernateTemplate.get(Teacher.class, 3);
        assertEquals(teacherToCreate, actual);
    }

    @Test
    void givenCorrectTeacherId_onFindById_shouldReturnOptionalWithCorrectTeacher() {
        var expected = Optional.of(expectedTeacher2);

        var actual = teacherDao.findById(2);

        assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectTeacherId_onFindById_shouldReturnEmptyOptional() {
        Optional<Teacher> expected = Optional.empty();

        var actual = teacherDao.findById(5);

        assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasTeachers_onFindAll_shouldReturnCorrectListOfTeachers() {
        assertEquals(expectedTeachers, teacherDao.findAll());
    }

    @Test
    void ifDatabaseHasNoTeachers_onFindAll_shouldReturnEmptyListOfTeachers() {
        hibernateTemplate.deleteAll(expectedTeachers);

        var teachers = teacherDao.findAll();

        assertThat(teachers).isEmpty();
    }

    @Test
    void givenTeacher_onUpdate_shouldUpdateCorrectly() {
        teacherDao.update(teacherToUpdate);

        var expected = hibernateTemplate.get(Teacher.class, 2);

        assertEquals(teacherToUpdate, expected);
    }

    @Test
    void givenCorrectTeacherId_onDelete_shouldDeleteCorrectly() {
        teacherDao.delete(expectedTeacher2);

        var expected = hibernateTemplate.get(Teacher.class, 2);
        assertNull(expected);
    }




    @Test
    void givenAddressId_onFindByAddressId_shouldReturnOptionalwithCorrectTeacher() {
        Optional<Teacher> expected = Optional.of(expectedTeacher1);

        Optional<Teacher> actual = teacherDao.findByAddressId(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNameAndEmail_onFindByNameAndEmail_shouldReturnOptionalwithCorrectTeacher() {
        Optional<Teacher> expected = Optional.of(expectedTeacher1);

        Optional<Teacher> actual = teacherDao.findByNameAndEmail("Adam", "Smith", "adam@smith.com");

        assertEquals(expected, actual);
    }

    @Test
    void givenLecture_onGetReplacementCandidates_shouldReturnCorrectListOfTeachers() {
        var expected = new ArrayList<Teacher>(Arrays.asList(expectedTeacher2));

        var actual = teacherDao.getReplacementCandidates(lectureToReplaceTeacher);

        assertEquals(expected, actual);
    }

    @Test
    void givenString_onFindBySubstring_shouldReturnCorrectListOfTeachers() {
        var expected = new ArrayList<Teacher>(Arrays.asList(expectedTeacher1));

        var actual = teacherDao.findBySubstring("adam");

        assertEquals(expected, actual);
    }

    @Test
    void givenNameAndEmail_onFindByNameAndEmail_shouldReturnOptionalWithCorrectTeacher() {
        var actual = teacherDao.findByNameAndEmail("Adam", "Smith", "adam@smith.com");

        assertEquals(Optional.of(expectedTeacher1), actual);
    }

    public interface TestData {
        List<Subject> testSubjects = new ArrayList<>(Arrays.asList(subjectToUpdate));
        List<Vacation> vacationsToCreate = new ArrayList<>(Arrays.asList(vacationToCreate));
        List<Vacation> vacationsToUpdate = new ArrayList<>(Arrays.asList(vacationToUpdate));

        Teacher teacherToCreate = Teacher.builder().firstName("Test").lastName("Teacher").id(3)
                .gender(Gender.MALE).degree(Degree.DOCTOR).subjects(testSubjects)
                .email("test@mail").phoneNumber("phone").address(addressToCreate)
                .vacations(vacationsToCreate).build();

        Teacher teacherToUpdate = Teacher.builder().firstName("Test").lastName("Teacher").id(2)
                .gender(Gender.MALE).degree(Degree.DOCTOR).subjects(testSubjects)
                .email("test@mail").phoneNumber("phone").address(addressToUpdate)
                .vacations(vacationsToUpdate).build();

        List<Vacation> expectedVacations1 = new ArrayList<>(Arrays.asList(expectedVacation1, expectedVacation2));
        List<Vacation> expectedVacations2 = new ArrayList<>(Arrays.asList(expectedVacation3, expectedVacation4));
        List<Subject> expectedSubjects1 = new ArrayList<>(Arrays.asList(expectedSubject1, expectedSubject2));
        List<Subject> expectedSubjects2 = new ArrayList<>(Arrays.asList(expectedSubject3, expectedSubject4));
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
