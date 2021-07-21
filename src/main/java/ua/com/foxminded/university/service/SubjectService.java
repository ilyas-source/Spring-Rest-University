package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.SubjectDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.exception.SubjectAssignedToTeacherException;
import ua.com.foxminded.university.exception.SubjectScheduledToLectureException;
import ua.com.foxminded.university.model.Subject;

@Service
public class SubjectService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

    private SubjectDao subjectDao;
    private LectureDao lectureDao;

    public SubjectService(SubjectDao subjectDao, LectureDao lectureDao) {
	this.subjectDao = subjectDao;
	this.lectureDao = lectureDao;
    }

    public void create(Subject subject) {
	logger.debug("Creating a new subject: {} ", subject);
	verifyNameIsNew(subject);
	subjectDao.create(subject);
    }

    public List<Subject> findAll() {
	return subjectDao.findAll();
    }

    public Optional<Subject> findById(int id) {
	return subjectDao.findById(id);
    }

    public void update(Subject subject) {
	logger.debug("Updating subject: {} ", subject);
	subjectDao.update(subject);
    }

    public void delete(int id) {
	logger.debug("Deleting subject by id: {} ", id);
	Optional<Subject> subject = subjectDao.findById(id);
	verifyExists(subject);
	verifyIsNotAssigned(subject);
	verifyIsNotScheduled(subject);
	subjectDao.delete(id);
    }

    private void verifyNameIsNew(Subject subject) {
	Optional<Subject> oldSubject = subjectDao.findByName(subject.getName());
	if (oldSubject.isPresent()) {
	    throw new EntityNotUniqueException(
		    String.format("Subject %s already exists, can't create duplicate", oldSubject.get().getName()));
	}
    }

    private void verifyExists(Optional<Subject> subject) {
	if (subject.isEmpty()) {
	    throw new EntityNotFoundException("Group not found, nothing to delete");
	}
    }

    private void verifyIsNotScheduled(Optional<Subject> subject) {
	if (!lectureDao.findBySubject(subject.get()).isEmpty()) {
	    throw new SubjectScheduledToLectureException("Subject is sheduled for 1 or more lectures, can't delete");
	}
    }

    private void verifyIsNotAssigned(Optional<Subject> subject) {
	if (subjectDao.countAssignments(subject.get()) > 0) {
	    throw new SubjectAssignedToTeacherException("Subject is assigned for 1 or more teachers, can't delete");
	}
    }
}
