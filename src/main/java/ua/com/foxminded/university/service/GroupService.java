package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.model.Group;

@Service
public class GroupService {

    private JdbcGroupDao jdbcGroupDao;

    public GroupService(JdbcGroupDao jdbcGroupDao) {
	this.jdbcGroupDao = jdbcGroupDao;
    }

    public void create(Group createGroup) {
	jdbcGroupDao.create(createGroup);
    }

    public List<Group> findAll() {
	return jdbcGroupDao.findAll();
    }

    public Optional<Group> findById(int choice) {
	return jdbcGroupDao.findById(choice);
    }

    public void update(Group newGroup) {
	jdbcGroupDao.update(newGroup);
    }

    public void delete(int id) {
	jdbcGroupDao.delete(id);
    }
}
