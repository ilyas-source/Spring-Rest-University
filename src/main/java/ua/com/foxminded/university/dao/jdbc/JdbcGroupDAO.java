package ua.com.foxminded.university.dao.jdbc;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.GroupDAO;
import ua.com.foxminded.university.model.Group;

@Component
public class JdbcGroupDAO implements GroupDAO {

    @Override
    public void create(Group e) {
	// TODO Auto-generated method stub

    }

    @Override
    public Optional<Group> findById(int id) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Group> findAll() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void update(Group e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void delete(int id) {
	// TODO Auto-generated method stub

    }

}
