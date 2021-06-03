package ua.com.foxminded.university.dao.jdbc;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.StudentDAO;
import ua.com.foxminded.university.model.Student;

@Component
public class JdbcStudentDAO implements StudentDAO {

    @Override
    public void create(Student e) {
	// TODO Auto-generated method stub

    }

    @Override
    public Optional<Student> findById(int id) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Student> findAll() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void update(Student e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void delete(int id) {
	// TODO Auto-generated method stub

    }

}
