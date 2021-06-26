package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.model.Subject;

@SpringJUnitConfig(SpringTestConfig.class)
class SubjectDaoTest {

    @Autowired
    private SubjectDao jdbcSubjectDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testCreate() {
	int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects") + 1;
	jdbcSubjectDao.create(new Subject("test", "test"));

	assertThat(expectedRows).isEqualTo(JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects"));
    }
}
