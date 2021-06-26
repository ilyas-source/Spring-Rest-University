package ua.com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.menu.SubjectsMenu;
import ua.com.foxminded.university.model.Subject;

//System.out.println(subjectsMenu.getStringOfSubjects(subjectDao.findAll()));
//System.out.println(subjectsMenu.getStringFromSubject(expected.get()));

@SpringJUnitConfig(SpringTestConfig.class)
@Sql({ "classpath:fill-subjects.sql" })
class SubjectDaoTest {

    @Autowired
    private SubjectDao subjectDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SubjectsMenu subjectsMenu;

    @Test
    void givenNewSubject_onCreate_shouldCreateSubject() {
	Subject subject = new Subject("test", "test");
	int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects") + 1;
	subjectDao.create(subject);
	System.out.println(subjectsMenu.getStringOfSubjects(subjectDao.findAll()));
	assertThat(expectedRows).isEqualTo(JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects"));
    }

    @Test
    void givenCorrectId_onFindById_shouldReturnOptionalWithCorrectSubject() {
	Optional<Subject> expected = Optional.of(new Subject(2, "Test Philosophy", "Base philosophy"));
	Optional<Subject> actual = subjectDao.findById(2);

	assertEquals(expected, actual);
    }

//    @Test
//    void testFindAll() {
//	fail("Not yet implemented");
//    }
////
//    @Test
//    void testUpdate() {
//	fail("Not yet implemented");
//    }
//
//    @Test
//    void testDelete() {
//	fail("Not yet implemented");
//    }
//
//    @Test
//    void testGetSubjectsByTeacher() {
//	fail("Not yet implemented");
//    }

}
