package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import ua.com.foxminded.university.model.Subject;

@Service
public class SubjectService {

    private JdbcSubjectDao jdbcSubjectDao;

    public SubjectService(JdbcSubjectDao jdbcSubjectDao) {
	this.jdbcSubjectDao = jdbcSubjectDao;
    }

    public void create(Subject createSubject) {
	jdbcSubjectDao.create(createSubject);
    }

    public List<Subject> findAll() {
	return jdbcSubjectDao.findAll();
    }

    public Optional<Subject> findById(int choice) {
	return jdbcSubjectDao.findById(choice);
    }

    public void update(Subject newSubject) {
	jdbcSubjectDao.update(newSubject);
    }

    public void delete(int id) {
	jdbcSubjectDao.delete(id);
    }
}
