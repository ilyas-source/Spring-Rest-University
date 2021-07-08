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
	// проверить что все студенты влезут
	// проверить что не выходной
	// проверить что препод не занят другой лекцией
	// проверить что препод не в отпуске
	// проверить что препод умеет это преподавать
	// проверить что студенты не заняты другой лекцией
	// проверить что аудитория не занята другой лекцией
	// проверить что не воскресенье
	lectureDao.create(createLecture);
    }

    public List<Lecture> findAll() {
	return lectureDao.findAll();
    }

    public Optional<Lecture> findById(int choice) {
	return lectureDao.findById(choice);
    }

    public void update(Lecture newLecture) {
	// проверить что все студенты влезут
	// проверить что не выходной
	// проверить что препод не занят другой лекцией
	// проверить что студенты не заняты другой лекцией
	// проверить что аудитория не занята другой лекцией
	// проверить что не воскресенье
	lectureDao.update(newLecture);
    }

    public void delete(int id) {
	// проверить что лекция существует
	lectureDao.delete(id);
    }
}
