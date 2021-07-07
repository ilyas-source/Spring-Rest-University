package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
import ua.com.foxminded.university.model.Classroom;

@Service
public class ClassroomService {

    private JdbcClassroomDao jdbcClassroomDao;

    public ClassroomService(JdbcClassroomDao jdbcClassroomDao) {
	this.jdbcClassroomDao = jdbcClassroomDao;
    }

    public void create(Classroom createClassroom) {
	jdbcClassroomDao.create(createClassroom);
    }

    public List<Classroom> findAll() {
	return jdbcClassroomDao.findAll();
    }

    public Optional<Classroom> findById(int choice) {
	return jdbcClassroomDao.findById(choice);
    }

    public void update(Classroom newClassroom) {
	jdbcClassroomDao.update(newClassroom);
    }

    public void delete(int id) {
	jdbcClassroomDao.delete(id);
    }
}
