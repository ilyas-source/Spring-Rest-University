package ua.com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.jdbc.JdbcLectureDao;
import ua.com.foxminded.university.model.Lecture;

//INSERT INTO lectures (date, begin_time, end_time, subject_id, teacher_id, classroom_id) VALUES
//('2000-1-1', '09:00:00', '10:00:00', 1, 1, 1),
//('2000-1-2', '10:00:00', '11:00:00', 2, 2, 2);

@SpringJUnitConfig(SpringTestConfig.class)
@Sql(scripts = { "classpath:schema.sql", "classpath:test-data.sql" })
class LectureDaoTest {

    private static final String TEST_WHERE_CLAUSE = "date='2000-01-01' AND name = 'test'"; // todo

    @Autowired
    private JdbcLectureDao lectureDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @Test
//    void givenNewLecture_onCreate_shouldCreateLecture() {
//	Lecture lecture = new Lecture(4, LocalDate.of(2000, 01, 01), "test");
//	int elementBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//		"lectures", "id = 4 AND " + TEST_WHERE_CLAUSE);
//
//	lectureDao.create(lecture);
//
//	int elementAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//		"lectures", "id = 4 AND " + TEST_WHERE_CLAUSE);
//
//	assertEquals(elementAfterCreate, elementBeforeCreate + 1);
//    }
//
//    @Test
//    void givenCorrectLectureId_onFindById_shouldReturnOptionalWithCorrectLecture() {
//	Optional<Lecture> expected = Optional.of(new Lecture(2, LocalDate.of(2000, 10, 30), "Halloween"));
//
//	Optional<Lecture> actual = lectureDao.findById(2);
//
//	assertEquals(expected, actual);
//    }
//
//    @Test
//    void givenIncorrectLectureId_onFindById_shouldReturnEmptyOptional() {
//	Optional<Lecture> expected = Optional.empty();
//
//	Optional<Lecture> actual = lectureDao.findById(5);
//
//	assertEquals(expected, actual);
//    }
//
//    @Test
//    void ifDatabaseHasLectures_onFindAll_shouldReturnCorrectListOfLectures() {
//	List<Lecture> expected = new ArrayList<>();
//	expected.add(new Lecture(1, LocalDate.of(2000, 12, 25), "Christmas"));
//	expected.add(new Lecture(2, LocalDate.of(2000, 10, 30), "Halloween"));
//	expected.add(new Lecture(3, LocalDate.of(2000, 03, 8), "International womens day"));
//
//	List<Lecture> actual = lectureDao.findAll();
//
//	assertEquals(expected, actual);
//    }
//
//    @Test
//    void ifDatabaseHasNoLectures_onFindAll_shouldReturnEmptyListOfLectures() {
//	JdbcTestUtils.deleteFromTables(jdbcTemplate, "lectures");
//
//	List<Lecture> lectures = lectureDao.findAll();
//
//	assertThat(lectures).isEmpty();
//    }
//
//    @Test
//    void givenLecture_onUpdate_shouldUpdateCorrectly() {
//	Lecture lecture = new Lecture(2, LocalDate.of(2000, 01, 01), "test");
//
//	lectureDao.update(lecture);
//
//	int elementAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
//		"lectures", "id = 2 AND " + TEST_WHERE_CLAUSE);
//
//	assertThat(elementAfterUpdate).isEqualTo(1);
//    }

    @Test
    void givenCorrectLectureId_onDelete_shouldDeleteCorrectly() {
	int elementBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures", "id = 2");

	lectureDao.delete(2);

	int elementAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures", "id = 2");

	assertEquals(elementAfterDelete, elementBeforeDelete - 1);
    }
}
