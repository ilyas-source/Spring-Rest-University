package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.exception.ClassroomBusyException;
import ua.com.foxminded.university.exception.ClassroomInvalidCapacityException;
import ua.com.foxminded.university.exception.ClassroomTooSmallException;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.model.Classroom;

@Service
public class ClassroomService {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomService.class);

    private ClassroomDao classroomDao;
    private LectureDao lectureDao;
    private LectureService lectureService;

    public ClassroomService(ClassroomDao classroomDao, LectureDao lectureDao, LectureService lectureService) {
	this.classroomDao = classroomDao;
	this.lectureDao = lectureDao;
	this.lectureService = lectureService;
    }

    public void create(Classroom classroom) {
	logger.debug("Creating a new classroom: {} ", classroom);
	verifyCapacityCorrect(classroom);
	verifyIsUniqueName(classroom);
	classroomDao.create(classroom);
    }

    public void update(Classroom classroom) {
	logger.debug("Updating classroom: {} ", classroom);
	verifyCapacityEnough(classroom);
	verifyIsUniqueName(classroom);
	classroomDao.update(classroom);
    }

    private void verifyIsUniqueName(Classroom classroom) {
	Optional<Classroom> classroomByName = classroomDao.findByName(classroom.getName());
	if ((classroomByName.isPresent() && (classroomByName.get().getId() != classroom.getId()))) {
	    throw new EntityNotUniqueException(String.format("Classroom with name %s already exists", classroom.getName()));
	}
    }

    private void verifyCapacityEnough(Classroom classroom) {
	int requiredCapacity = lectureDao.findByClassroom(classroom)
		.stream()
		.map(lectureService::countStudentsInLecture)
		.mapToInt(v -> v)
		.max().orElse(0);
	if (classroom.getCapacity() < requiredCapacity) {
	    throw new ClassroomTooSmallException("Classroom too small for scheduled lectures");
	}
    }

    private void verifyCapacityCorrect(Classroom classroom) {
	if (classroom.getCapacity() < 1) {
	    throw new ClassroomInvalidCapacityException("Classroom capacity invalid");
	}
    }

    public List<Classroom> findAll() {
	return classroomDao.findAll();
    }

    public Optional<Classroom> findById(int id) {
	return classroomDao.findById(id);
    }

    public void delete(int id) {
	logger.debug("Deleting classroom by id: {} ", id);
	Optional<Classroom> classroom = classroomDao.findById(id);
	if (classroom.isEmpty()) {
	    throw new EntityNotFoundException(String.format("Classroom with id #%s not found", id));
	}
	verifyHasNoLectures(classroom.get());
	classroomDao.delete(id);

    }

    private void verifyHasNoLectures(Classroom classroom) {
	if (!lectureDao.findByClassroom(classroom).isEmpty()) {
	    throw new ClassroomBusyException("There are scheduled lectures, can't delete classroom");
	}
    }
}
