package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.jdbc.JdbcLectureDao;
import ua.com.foxminded.university.model.Lecture;

@Service
public class LectureService {

    private JdbcLectureDao jdbcLectureDao;

    public LectureService(JdbcLectureDao jdbcLectureDao) {
	this.jdbcLectureDao = jdbcLectureDao;
    }

    public void create(Lecture createLecture) {
	jdbcLectureDao.create(createLecture);
    }

    public List<Lecture> findAll() {
	return jdbcLectureDao.findAll();
    }

    public Optional<Lecture> findById(int choice) {
	return jdbcLectureDao.findById(choice);
    }

    public void update(Lecture newLecture) {
	jdbcLectureDao.update(newLecture);
    }

    public void delete(int id) {
	jdbcLectureDao.delete(id);
    }
}
