package ua.com.foxminded.university.dao.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.dao.jdbc.mappers.SubjectMapper;
import ua.com.foxminded.university.model.Subject;

import java.util.List;

//@Component
public class JdbcSubjectDao {

    private static final String COUNT_ASSIGNMENTS = "SELECT COUNT(*) FROM teachers_subjects WHERE subject_id = ?";
    private static final String FIND_BY_TEACHER_ID = "SELECT s.id, s.name, s.description from teachers_subjects " +
            "AS t_s LEFT JOIN subjects AS s ON (t_s.subject_id=s.id) WHERE t_s.teacher_id=?;";

    private JdbcTemplate jdbcTemplate;
    private SubjectMapper subjectMapper;

    public JdbcSubjectDao(JdbcTemplate jdbcTemplate, SubjectMapper subjectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.subjectMapper = subjectMapper;
    }

    public List<Subject> getByTeacherId(int id) {
        return jdbcTemplate.query(FIND_BY_TEACHER_ID, subjectMapper, id);
    }

    public int countAssignments(Subject subject) {
        return jdbcTemplate.queryForObject(COUNT_ASSIGNMENTS, Integer.class, subject.getId());
    }
}
