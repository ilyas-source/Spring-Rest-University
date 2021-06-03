package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.*;
import ua.com.foxminded.university.dao.jdbc.mappers.SubjectMapper;
import ua.com.foxminded.university.model.Subject;

@Component
public class JdbcSubjectDAO implements SubjectDAO {

    private static final String CREATE_SUBJECT = "INSERT INTO subjects (name, description) VALUES (?, ?)";
    private static final String FIND_SUBJECT_BY_ID = "SELECT * FROM subjects WHERE id = ?";
    private static final String FIND_ALL_SUBJECTS = "SELECT * FROM subjects";
    private static final String UPDATE_SUBJECT = "UPDATE subjects SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_SUBJECT_BY_ID = "DELETE FROM subjects WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final SubjectMapper subjectMapper;

    public JdbcSubjectDAO(JdbcTemplate jdbcTemplate, SubjectMapper subjectMapper) {
	this.jdbcTemplate = jdbcTemplate;
	this.subjectMapper = subjectMapper;
    }

    @Override
    public void create(Subject subject) {
	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcTemplate.update(connection -> {
	    PreparedStatement ps = connection
		    .prepareStatement(CREATE_SUBJECT, Statement.RETURN_GENERATED_KEYS);
	    ps.setString(1, subject.getName());
	    ps.setString(2, subject.getDescription());
	    return ps;
	}, keyHolder);
	subject.setId((int) keyHolder.getKeys().get("id"));
    }

    @Override
    public Optional<Subject> findById(int id) {
	return Optional.of(jdbcTemplate.queryForObject(FIND_SUBJECT_BY_ID, subjectMapper, id));
    }

    @Override
    public List<Subject> findAll() {
	return jdbcTemplate.query(FIND_ALL_SUBJECTS, subjectMapper);
    }

    @Override
    public void update(Subject subject) {
	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcTemplate.update(connection -> {
	    PreparedStatement ps = connection
		    .prepareStatement(UPDATE_SUBJECT, Statement.RETURN_GENERATED_KEYS);
	    ps.setString(1, subject.getName());
	    ps.setString(2, subject.getDescription());
	    ps.setInt(3, subject.getId());
	    return ps;
	}, keyHolder);
	subject.setId((int) keyHolder.getKeys().get("id"));
    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_SUBJECT_BY_ID, id);
    }

}
