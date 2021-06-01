package ua.com.foxminded.university.dao.jdbc;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.*;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.mappers.*;

@Component
public class JDBCSubjectDAO implements SubjectDAO {

    JdbcTemplate jdbcTemplate;
    SubjectMapper subjectMapper;

    private static final String CREATE_SUBJECT = "INSERT INTO subjects (name, description) VALUES (?, ?)";
    private static final String FIND_SUBJECT_BY_ID = "SELECT * FROM subjects WHERE id = ?";
    private static final String FIND_ALL_SUBJECTS = "SELECT * FROM subjects";
    private static final String UPDATE_SUBJECT = "UPDATE subjects SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_SUBJECT_BY_ID = "DELETE FROM subjects WHERE id = ?";

    @Autowired
    public JDBCSubjectDAO(JdbcTemplate jdbcTemplate, SubjectMapper subjectMapper) {
	this.jdbcTemplate = jdbcTemplate;
	this.subjectMapper = subjectMapper;
    }

    @Override
    public void create(Subject subject) {
	jdbcTemplate.update(CREATE_SUBJECT, subject.getName(), subject.getDescription());
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
	jdbcTemplate.update(UPDATE_SUBJECT, subject.getName(), subject.getDescription());
    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_SUBJECT_BY_ID, id);
    }

}
