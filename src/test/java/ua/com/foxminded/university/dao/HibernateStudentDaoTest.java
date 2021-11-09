package ua.com.foxminded.university.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.ReflectionTestUtils;
import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.hibernate.HibernateStudentDao;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Student;

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
import static ua.com.foxminded.university.dao.HibernateGroupDaoTest.TestData.expectedGroup1;
import static ua.com.foxminded.university.dao.HibernateGroupDaoTest.TestData.expectedGroup2;
import static ua.com.foxminded.university.dao.HibernateStudentDaoTest.TestData.*;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class HibernateStudentDaoTest {

    private static final String defaultSortDirection = "ASC";
    private static final String defaultSortAttribute = "last_name";

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(studentDao, "defaultSortDirection", defaultSortDirection);
        ReflectionTestUtils.setField(studentDao, "defaultSortAttribute", defaultSortAttribute);
    }

    @Autowired
    private HibernateStudentDao studentDao;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    void givenNewStudent_onCreate_shouldCreateStudent() {
        var actual = hibernateTemplate.get(Student.class, 5);
        assertNull(actual);

        studentDao.create(studentToCreate);

        actual = hibernateTemplate.get(Student.class, 5);
        assertEquals(studentToCreate, actual);
    }

    @Test
    void givenCorrectStudentId_onFindById_shouldReturnOptionalWithCorrectStudent() {
        var expected = Optional.of(expectedStudent2);

        var actual = studentDao.findById(2);

        assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectStudentId_onFindById_shouldReturnEmptyOptional() {
        Optional<Student> expected = Optional.empty();

        var actual = studentDao.findById(5);

        assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasStudents_onFindAll_shouldReturnCorrectListOfStudents() {
        assertEquals(expectedStudents, studentDao.findAll());
    }

    @Test
    void ifDatabaseHasNoStudents_onFindAll_shouldReturnEmptyListOfStudents() {
        hibernateTemplate.deleteAll(expectedStudents);

        var students = studentDao.findAll();

        assertThat(students).isEmpty();
    }

    @Test
    void givenStudent_onUpdate_shouldUpdateCorrectly() {
        studentDao.update(studentToUpdate);

        var expected = hibernateTemplate.get(Student.class, 2);

        assertEquals(studentToUpdate, expected);
    }

    @Test
    void givenCorrectStudentId_onDelete_shouldDeleteCorrectly() {
        studentDao.delete(expectedStudent2);

        var expected = hibernateTemplate.get(Student.class, 2);
        assertNull(expected);
    }

    @Test
    void givenNameAndBirthdate_onFindByNameAndBirthdate_shouldReturnOptionalWithCorrectStudent() {
        Optional<Student> expected = Optional.of(expectedStudent1);

        Optional<Student> actual = studentDao.findByNameAndBirthDate("Ivan", "Petrov", LocalDate.of(1980, 11, 1));

        assertEquals(expected, actual);
    }

    @Test
    void givenWrongData_onFindByNameAndBirthdate_shouldReturnOptionalEmpty() {
        Optional<Student> actual = studentDao.findByNameAndBirthDate("Ivan1", "Petrov", LocalDate.of(1980, 11, 1));

        assertEquals(Optional.empty(), actual);
    }

    @Test
    void givenGroup_onFindByGroup_shouldReturnCorrectListOfStudents() {
        List<Student> expected = new ArrayList<>(Arrays.asList(expectedStudent1, expectedStudent3));

        List<Student> actual = studentDao.findByGroup(expectedGroup1);

        assertEquals(expected, actual);
    }

    @Test
    void givenGroup_onCountStudentsInGroup_shouldReturnCorrectNumber() {
        int expected = 2;

        int actual = studentDao.countInGroup(expectedGroup1);

        assertEquals(expected, actual);
    }

    @Test
    void givenSubstring_onFindBySubstring_thenReturnCorrectListOfStudents() {
        var expected = new ArrayList<>(List.of(expectedStudent1));

        var actual = studentDao.findBySubstring("Ivan");

        assertEquals(expected, actual);
    }

    @Test
    void givenPageable_onFindAll_shouldReturnCorrectPageOfStudents() {
        Pageable pageable = PageRequest.of(1, 2, Sort.by("id").ascending());

        Page<Student> expected = new PageImpl<>(expectedStudentsPage, pageable, 4);
        var actual = studentDao.findAll(pageable);

        assertEquals(expected, actual);
    }

    public interface TestData {
        Student studentToCreate = Student.builder().firstName("Name").lastName("Lastname")
                .id(5).gender(Gender.MALE).birthDate(LocalDate.of(1980, 2, 2))
                .email("test@mail").phone("+phone").address(expectedAddress3)
                .group(expectedGroup2).build();
        Student studentToUpdate = Student.builder().firstName("Name").lastName("Lastname")
                .id(2).gender(Gender.MALE).birthDate(LocalDate.of(1980, 2, 2))
                .email("test@mail").phone("+phone").address(expectedAddress3)
                .group(expectedGroup2).build();

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
        List<Student> expectedStudentsPage = new ArrayList<>(
                Arrays.asList(expectedStudent3, expectedStudent4));
    }
}