package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

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
	verifyCapacityCorrect(newClassroom);
	verifyNameIsNew(newClassroom);
//	verifyCapacityIsEnough(newClassroom);
	classroomDao.update(newClassroom);
    }

//    private void verifyCapacityIsEnough(Classroom newClassroom) {
//	Stream<Lecture> lecturesInClassroom = lectureDao.findByClassroom(newClassroom).stream();
//	
//	
//						.flatMap(p->Stream.of(p.getGroups()))
//						.flatMap(p->Stream.of(groupDao.countStudents(p)))
//						.reduce(0,Integer::sum);

//	Address address = addressDao.findById(id).orElseThrow(() -> new Exception("Address not found"));
//	List<Address> teacherAddresses = teacherDao.findAll()
//		.stream()
//		.flatMap(p -> Stream.of(p.getAddress()))
//		.collect(toList());
//	if (teacherAddresses.contains(address)) {
//	    throw new Exception("Address is used by a teacher account, can't delete address");
//	}

    public void delete(int id) {
//	verifyIdExists(id);
//	verifyTheresNoLectures(classroomDao.findById(id).get());
	classroomDao.delete(id);
    }

    private void verifyNameIsNew(Classroom classroom) throws Exception {
	if (groupDao.findByName(classroom.getName()).isPresent()) {
	    throw new Exception(String.format("Classroom with name %s already exists, can't create", classroom.getName()));
	}
    }

    private void verifyCapacityCorrect(Classroom classroom) throws Exception {
	if (classroom.getCapacity() < 1) {
	    throw new Exception("Wrong classroom capacity, can't create");
	}
    }
}
