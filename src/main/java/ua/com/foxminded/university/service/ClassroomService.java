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
	boolean canCreate = capacityIsCorrect(classroom) && nameIsNew(classroom);
	if (canCreate) {
	    classroomDao.create(classroom);
	} else {
	    System.out.println("Can't create classroom");
	}
    }

    public List<Classroom> findAll() {
	return classroomDao.findAll();
    }

    public Optional<Classroom> findById(int choice) {
	return classroomDao.findById(choice);
    }

    public void update(Classroom newClassroom) {
	boolean canUpdate = nameIsNew(newClassroom) && verifyCapacityIsEnough(newClassroom);
	if (canUpdate) {
	    classroomDao.update(newClassroom);
	} else {
	    System.out.println("Can't update classroom");
	}
    }

    public void delete(int id) {
	boolean canDelete = idExists(id) && theresNoLectures(classroomDao.findById(id).get());
	if (canDelete) {
	    classroomDao.delete(id);
	} else {
	    System.out.println("Can't delete classroom");
	}
    }

    private boolean verifyCapacityIsEnough(Classroom newClassroom) {
	int requiredCapacity = lectureDao.findByClassroom(newClassroom)
		.stream()
		.flatMap(l -> Stream.of(lectureService.countStudentsInLecture(l)))
		.mapToInt(v -> v)
		.max().orElse(0);

	return newClassroom.getCapacity() > requiredCapacity;
    }

    private boolean theresNoLectures(Classroom classroom) {
	return lectureDao.findByClassroom(classroom).isEmpty();
    }

    private boolean idExists(int id) {
	return classroomDao.findById(id).isPresent();

    }

    private boolean nameIsNew(Classroom classroom) {
	return classroomDao.findByName(classroom.getName()).isEmpty();
    }

    private boolean capacityIsCorrect(Classroom classroom) {
	return classroom.getCapacity() > 0;
    }
}
