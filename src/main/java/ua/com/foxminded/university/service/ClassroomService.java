package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.exception.ClassroomInvalidCapacityException;
import ua.com.foxminded.university.exception.ClassroomOccupiedException;
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
	verifyCapacityIsCorrect(classroom);
	verifyNameIsUnique(classroom);
	classroomDao.create(classroom);
    }

    public List<Classroom> findAll() {
	return classroomDao.findAll();
    }

    public Optional<Classroom> findById(int id) {
	return classroomDao.findById(id);
    }

    public void update(Classroom classroom) {
	logger.debug("Updating classroom: {} ", classroom);
	verifyCapacityIsEnough(classroom);
	verifyNameIsUnique(classroom);
	classroomDao.update(classroom);
    }

    public void delete(int id) {
	logger.debug("Deleting classroom by id: {} ", id);
	var classroom = classroomDao.findById(id).orElseThrow(
		() -> new EntityNotFoundException(String.format("Classroom id:%s not found, nothing to delete", id)));
	verifyHasNoLectures(classroom);
	classroomDao.delete(id);
    }

    private void verifyNameIsUnique(Classroom classroom) {
	classroomDao.findByName(classroom.getName())
		.filter(c -> c.getId() != classroom.getId())
		.ifPresent(c -> {
		    throw new EntityNotUniqueException(
			    String.format("Classroom %s already exists", classroom.getName()));
		});
    }

    private void verifyCapacityIsEnough(Classroom classroom) {
	int requiredCapacity = lectureDao.findByClassroom(classroom)
		.stream()
		.map(lectureService::countStudentsInLecture)
		.mapToInt(v -> v)
		.max().orElse(0);
	if (classroom.getCapacity() < requiredCapacity) {
	    throw new ClassroomInvalidCapacityException(
		    String.format("Classroom too small: required %s, but was %s", requiredCapacity, classroom.getCapacity()));
	}
    }

    private void verifyCapacityIsCorrect(Classroom classroom) {
	if (classroom.getCapacity() < 1) {
	    throw new ClassroomInvalidCapacityException("Classroom capacity should not be less than 1");
	}
    }

    private void verifyHasNoLectures(Classroom classroom) {
	if (!lectureDao.findByClassroom(classroom).isEmpty()) {
	    throw new ClassroomOccupiedException(
		    String.format("There are scheduled lectures, can't delete classroom %s", classroom.getName()));
	}
    }
}
