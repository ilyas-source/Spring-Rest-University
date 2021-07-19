package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.SubjectDao;
import ua.com.foxminded.university.model.Subject;

@Service
public class SubjectService {

    private SubjectDao subjectDao;
    private LectureDao lectureDao;

    public SubjectService(SubjectDao subjectDao, LectureDao lectureDao) {
	this.subjectDao = subjectDao;
	this.lectureDao = lectureDao;
    }

    public void create(Subject subject) {
	if (nameIsNew(subject)) {
	    subjectDao.create(subject);
	}
    }

    private boolean nameIsNew(Subject subject) {
	return subjectDao.findByName(subject.getName()).isEmpty();
    }

    public List<Subject> findAll() {
	return subjectDao.findAll();
    }

    public Optional<Subject> findById(int id) {
	return subjectDao.findById(id);
    }

    public void update(Subject subject) {
	subjectDao.update(subject);
    }

    public void delete(int id) {
	Optional<Subject> subject = subjectDao.findById(id);
	var canDelete = subject.isPresent()
		&& isNotAssigned(subject.get())
		&& isNotScheduled(subject.get());
	if (canDelete) {
	    subjectDao.delete(id);
	}
    }

    private boolean isNotScheduled(Subject subject) {
	return lectureDao.findBySubject(subject).isEmpty();
    }

    private boolean isNotAssigned(Subject subject) {
	return subjectDao.countAssignments(subject) == 0;
    }
}
