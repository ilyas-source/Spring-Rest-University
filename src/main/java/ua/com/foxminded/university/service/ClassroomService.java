package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.model.Classroom;

@Service
public class ClassroomService {

    private ClassroomDao classroomDao;
    private JdbcGroupDao groupDao;
    private LectureDao lectureDao;

    public ClassroomService(ClassroomDao classroomDao, JdbcGroupDao groupDao, LectureDao lectureDao) {
	this.classroomDao = classroomDao;
	this.groupDao = groupDao;
	this.lectureDao = lectureDao;
    }

    public void create(Classroom classroom) throws Exception {
	verifyCapacityCorrect(classroom);
	verifyNameIsNew(classroom);
	classroomDao.create(classroom);
    }

    public List<Classroom> findAll() {
	return classroomDao.findAll();
    }

    public Optional<Classroom> findById(int choice) {
	return classroomDao.findById(choice);
    }

    public void update(Classroom newClassroom) throws Exception {
	verifyNameIsNew(newClassroom);
	verifyCapacityIsEnough(newClassroom);
	classroomDao.update(newClassroom);
    }

    private void verifyCapacityIsEnough(Classroom newClassroom) throws Exception {
	int requiredCapacity = lectureDao.findByClassroom(newClassroom)
		.stream()
		.flatMap(l -> Stream.of(lectureDao.countStudentsInLecture(l)))
		.mapToInt(v -> v)
		.max()
		.orElse(1);

	if (newClassroom.getCapacity() < requiredCapacity) {
	    throw new Exception(
		    String.format("Required minimum capacity %s, but was %s", requiredCapacity, newClassroom.getCapacity()));
	}
    }

    public void delete(int id) throws Exception {
	verifyIdExists(id);
	verifyTheresNoLectures(classroomDao.findById(id).get());
	classroomDao.delete(id);
    }

    private void verifyTheresNoLectures(Classroom classroom) throws Exception {
	if (!lectureDao.findByClassroom(classroom).isEmpty()) {
	    throw new Exception("Classroom has scheduled lecture(s), can't delete");
	}
    }

    private void verifyIdExists(int id) throws Exception {
	if (classroomDao.findById(id).isEmpty()) {
	    throw new Exception(String.format("Classroom with id %s does not exist, nothing to delete", id));
	}
    }

    private void verifyNameIsNew(Classroom classroom) throws Exception {
	if (classroomDao.findByName(classroom.getName()).isPresent()) {
	    throw new Exception(String.format("Classroom with name %s already exists, can't create", classroom.getName()));
	}
    }

    private void verifyCapacityCorrect(Classroom classroom) throws Exception {
	if (classroom.getCapacity() < 1) {
	    throw new Exception("Wrong classroom capacity, can't create");
	}
    }
}