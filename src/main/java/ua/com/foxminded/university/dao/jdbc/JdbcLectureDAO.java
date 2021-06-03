package ua.com.foxminded.university.dao.jdbc;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.LectureDAO;
import ua.com.foxminded.university.model.Lecture;

@Component
public class JdbcLectureDAO implements LectureDAO {

    @Override
    public void create(Lecture e) {
	// TODO Auto-generated method stub

    }

    @Override
    public Optional<Lecture> findById(int id) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Lecture> findAll() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void update(Lecture e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void delete(int id) {
	// TODO Auto-generated method stub

    }

}
