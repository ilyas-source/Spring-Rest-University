package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

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
	if (!isCapacityEnough(classroom)) {
	    return;
	}
	if (!idExists(classroom.getId())) {
	    return;
	}
	var idIsTheSame = false;
	Optional<Classroom> classroomByName = classroomDao.findByName(classroom.getName());
	if (classroomByName.isPresent()) {
	    idIsTheSame = (classroomByName.get().getId() == classroom.getId());
	}
	if (idIsTheSame) {
	    classroomDao.update(classroom);
	}
    }

    private boolean hasNewName(Classroom classroom) {
	return classroomDao.findByName(classroom.getName()).isEmpty();
    }

    public void delete(int id) {
	if (classroomDao.findById(id)
		.filter(this::hasNoLectures)
		.isPresent()) {
	    classroomDao.delete(id);
	}
    }

    private boolean isCapacityEnough(Classroom classroom) {
	int requiredCapacity = lectureDao.findByClassroom(classroom)
		.stream()
		.map(lectureService::countStudentsInLecture)
		.mapToInt(v -> v)
		.max().orElse(0);
	return classroom.getCapacity() >= requiredCapacity;
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
