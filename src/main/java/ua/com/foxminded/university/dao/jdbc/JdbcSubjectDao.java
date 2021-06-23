package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.*;
import ua.com.foxminded.university.dao.jdbc.mappers.SubjectMapper;
import ua.com.foxminded.university.model.Subject;

@Component
public class JdbcSubjectDao implements SubjectDao {

    private static final String CREATE = "INSERT INTO subjects (name, description) VALUES (?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM subjects WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM subjects";
    private static final String UPDATE = "UPDATE subjects SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM subjects WHERE id = ?";
    private static final String FIND_BY_TEACHER_ID = "SELECT s.id, s.name, s.description from teachers_subjects " +
	    "AS t_s LEFT JOIN subjects AS s ON (t_s.subject_id=s.id) WHERE t_s.teacher_id=?;";

    private JdbcTemplate jdbcTemplate;
    private SubjectMapper subjectMapper;

    public JdbcSubjectDao(JdbcTemplate jdbcTemplate, SubjectMapper subjectMapper) {
	this.jdbcTemplate = jdbcTemplate;
	this.subjectMapper = subjectMapper;
    }

    @Override
    public void addToDb(Subject subject) {
	List<Subject> subjects = findAll();
	if (!subjects.contains(subject)) {
	    KeyHolder keyHolder = new GeneratedKeyHolder();

	    jdbcTemplate.update(connection -> {
		PreparedStatement ps = connection
			.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, subject.getName());
		ps.setString(2, subject.getDescription());
		return ps;
	    }, keyHolder);
	    subject.setId((int) keyHolder.getKeys().get("id"));
	}
    }

    @Override
    public Optional<Subject> findById(int id) {
	try {
	    return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, subjectMapper, id));
	} catch (EmptyResultDataAccessException e) {
	    return Optional.empty();
	}
    }

    @Override
    public List<Subject> findAll() {
	return jdbcTemplate.query(FIND_ALL, subjectMapper);
    }

    @Override
    public void update(Subject subject) {
	jdbcTemplate.update(UPDATE, subject.getName(), subject.getDescription(), subject.getId());
    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }

    public List<Subject> getSubjectsByTeacher(int id) {
	return jdbcTemplate.query(FIND_BY_TEACHER_ID, subjectMapper, id);
    }
}