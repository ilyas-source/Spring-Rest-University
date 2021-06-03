package ua.com.foxminded.university.dao.jdbc;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.ClassroomDAO;
import ua.com.foxminded.university.model.Classroom;

@Component
public class JdbcClassroomDAO implements ClassroomDAO {

    @Override
    public void create(Classroom e) {
	// TODO Auto-generated method stub

    }

    @Override
    public Optional<Classroom> findById(int id) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Classroom> findAll() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void update(Classroom e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void delete(int id) {
	// TODO Auto-generated method stub

    }

}
