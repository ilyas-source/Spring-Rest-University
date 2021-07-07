package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.model.Lecture;

@Service
public class LectureService {

    private LectureDao lectureDao;

    public LectureService(LectureDao lectureDao) {
	this.lectureDao = lectureDao;
    }

    public void create(Lecture createLecture) {
	lectureDao.create(createLecture);
    }

    public List<Lecture> findAll() {
	return lectureDao.findAll();
    }

    public Optional<Lecture> findById(int choice) {
	return lectureDao.findById(choice);
    }

    public void update(Lecture newLecture) {
	lectureDao.update(newLecture);
    }

    public void delete(int id) {
	lectureDao.delete(id);
    }
}
