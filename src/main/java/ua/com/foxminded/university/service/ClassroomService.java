package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.model.Classroom;

@Service
public class ClassroomService {

    private ClassroomDao classroomDao;
    private LectureDao lectureDao;
    private LectureService lectureService;

    public ClassroomService(ClassroomDao classroomDao, LectureDao lectureDao, LectureService lectureService) {
	this.classroomDao = classroomDao;
	this.lectureDao = lectureDao;
	this.lectureService = lectureService;
    }

    public void create(Classroom classroom) {
	var canCreate = isCapacityCorrect(classroom) && hasNewName(classroom);
	if (canCreate) {
	    classroomDao.create(classroom);
	}
    }

    public List<Classroom> findAll() {
	return classroomDao.findAll();
    }

    public Optional<Classroom> findById(int id) {
	return classroomDao.findById(id);
    }

    public void update(Classroom classroom) {
	var idExists = idExists(classroom.getId());
	var idIsTheSame = false;
	Optional<Classroom> classroomByName = classroomDao.findByName(classroom.getName());
	if (classroomByName.isPresent()) {
	    idIsTheSame = (classroomByName.get().getId() == classroom.getId());
	}
	if (isCapacityEnough(classroom) && idExists && idIsTheSame) {
	    classroomDao.update(classroom);
	}
    }

    private boolean hasNewName(Classroom classroom) {
	return classroomDao.findByName(classroom.getName()).isEmpty();
    }

    public void delete(int id) {
	Optional<Classroom> classroom = classroomDao.findById(id);
	var canDelete = classroom.isPresent() && hasNoLectures(classroom.get());
	if (canDelete) {
	    classroomDao.delete(id);
	}
    }

    private boolean isCapacityEnough(Classroom newClassroom) {
	int requiredCapacity = lectureDao.findByClassroom(newClassroom)
		.stream()
		.flatMap(l -> Stream.of(lectureService.countStudentsInLecture(l)))
		.mapToInt(v -> v)
		.max().orElse(0);

	return newClassroom.getCapacity() >= requiredCapacity;
    }

    private boolean hasNoLectures(Classroom classroom) {
	return lectureDao.findByClassroom(classroom).isEmpty();
    }

    private boolean isCapacityCorrect(Classroom classroom) {
	return classroom.getCapacity() > 0;
    }

    private boolean idExists(int id) {
	return classroomDao.findById(id).isPresent();
    }
}
