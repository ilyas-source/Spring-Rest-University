package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.model.Classroom;

@Service
public class ClassroomService {

    private ClassroomDao classroomDao;

    public ClassroomService(ClassroomDao classroomDao) {
	this.classroomDao = classroomDao;
    }

    public void create(Classroom createClassroom) {
	classroomDao.create(createClassroom);
    }

    public List<Classroom> findAll() {
	return classroomDao.findAll();
    }

    public Optional<Classroom> findById(int choice) {
	return classroomDao.findById(choice);
    }

    public void update(Classroom newClassroom) {
	classroomDao.update(newClassroom);
    }

    public void delete(int id) {
	classroomDao.delete(id);
    }
}
