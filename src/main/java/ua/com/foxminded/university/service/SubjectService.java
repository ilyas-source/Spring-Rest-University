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
	verifyNameIsUnique(subject);
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
	verifyNameIsUnique(subject);
	subjectDao.update(subject);
    }

    public void delete(int id) {
	logger.debug("Deleting subject by id: {} ", id);
	Optional<Subject> subject = subjectDao.findById(id);
	if (subject.isEmpty()) {
	    throw new EntityNotFoundException(String.format("Subject with id:%s not found, nothing to delete", id));
	}
	verifyIsNotAssigned(subject.get());
	verifyIsNotScheduled(subject.get());
	subjectDao.delete(id);
    }

    private void verifyNameIsUnique(Subject subject) {
	Optional<Subject> subjectByName = subjectDao.findByName(subject.getName());
	if (subjectByName.isPresent() && (subjectByName.get().getId() != subject.getId())) {
	    throw new EntityNotUniqueException(
		    String.format("Subject %s already exists, can't create duplicate", subjectByName.get().getName()));
	}
    }

    private void verifyIsNotScheduled(Subject subject) {
	if (!lectureDao.findBySubject(subject).isEmpty()) {
	    throw new SubjectScheduledToLectureException(
		    String.format("Subject %s is sheduled for lecture(s), can't delete", subject.getName()));
	}
    }

    private void verifyIsNotAssigned(Subject subject) {
	if (subjectDao.countAssignments(subject) > 0) {
	    throw new SubjectAssignedToTeacherException(
		    String.format("Subject %s is assigned to teacher(s), can't delete", subject.getName()));
	}
    }
}
