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
	} else {
	    System.out.println("Subject with this name already exists, can't create");
	}
    }

    private boolean nameIsNew(Subject subject) {
	return subjectDao.findByName(subject.getName()).isEmpty();
    }

    public List<Subject> findAll() {
	return subjectDao.findAll();
    }

    public Optional<Subject> findById(int choice) {
	return subjectDao.findById(choice);
    }

    public void update(Subject subject) {
	subjectDao.update(subject);
    }

    public void delete(int id) {
	boolean canDelete = idExists(id);
	canDelete = canDelete && subjectIsNotAssigned(subjectDao.findById(id).get());
	canDelete = canDelete && subjectIsNotScheduled(subjectDao.findById(id).get());
	if (canDelete) {
	    subjectDao.delete(id);
	} else {
	    System.out.println("Can't delete subject");
	}
    }

    private boolean subjectIsNotScheduled(Subject subject) {
	boolean result = lectureDao.findBySubject(subject).isEmpty();
	System.out.println("Subject is not scheduled to lectures: " + result);
	return result;
    }

    private boolean subjectIsNotAssigned(Subject subject) {
	boolean result = subjectDao.countAssignments(subject) == 0;
	System.out.println("Subject is not assigned to teachers: " + result);
	return result;
    }

    private boolean idExists(int id) {
	return subjectDao.findById(id).isPresent();
    }
}
