package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.exception.EntityInUseException;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.repository.LectureRepository;
import ua.com.foxminded.university.repository.SubjectRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class SubjectService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

    private SubjectRepository subjectRepository;
    private LectureRepository lectureRepository;

    public SubjectService(SubjectRepository subjectRepository, LectureRepository lectureRepository) {
        this.subjectRepository = subjectRepository;
        this.lectureRepository = lectureRepository;
    }

    public void create(Subject subject) {
        logger.debug("Creating a new subject: {} ", subject);
        verifyNameIsUnique(subject);
        subjectRepository.save(subject);
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> findById(int id) {
        return subjectRepository.findById(id);
    }

    public Subject getById(int id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find subject by id " + id));
    }

    public void update(Subject subject) {
        logger.debug("Updating subject: {} ", subject);
        verifyNameIsUnique(subject);
        subjectRepository.save(subject);
    }

    public void delete(int id) {
        logger.debug("Deleting subject by id: {} ", id);
        var subject = getById(id);
        verifyIsNotAssigned(subject);
        verifyIsNotScheduled(subject);
        subjectRepository.delete(subject);
    }

    private void verifyNameIsUnique(Subject subject) {
        subjectRepository.findByName(subject.getName())
                .filter(s -> s.getId() != subject.getId())
                .ifPresent(s -> {
                    throw new EntityNotUniqueException(String.format("Subject %s already exists", s.getName()));
                });
    }

    private void verifyIsNotScheduled(Subject subject) {
        if (!lectureRepository.findBySubject(subject).isEmpty()) {
            throw new EntityInUseException(
                    String.format("Subject %s is scheduled for lecture(s), can't delete", subject.getName()));
        }
    }

    private void verifyIsNotAssigned(Subject subject) {
        if (subjectRepository.countAssignments(subject) > 0) {
            throw new EntityInUseException(
                    String.format("Subject %s is assigned to teacher(s), can't delete", subject.getName()));
        }
    }
}
