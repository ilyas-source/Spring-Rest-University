package ua.com.foxminded.university.dao.jdbc;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.TeacherDAO;
import ua.com.foxminded.university.model.Teacher;

@Component
public class JdbcTeacherDAO implements TeacherDAO {

    @Override
    public void create(Teacher e) {
	// TODO Auto-generated method stub

    }

    @Override
    public Optional<Teacher> findById(int id) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Teacher> findAll() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void update(Teacher e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void delete(int id) {
	// TODO Auto-generated method stub

    }

}
