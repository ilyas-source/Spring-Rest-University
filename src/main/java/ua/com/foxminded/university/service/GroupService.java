package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.model.Group;

@Service
public class GroupService {

    private JdbcGroupDao groupDao;

    public GroupService(JdbcGroupDao jdbcGroupDao) {
	this.groupDao = jdbcGroupDao;
    }

    public void create(Group group) throws Exception {
	verifyNameIsNew(group);
	groupDao.create(group);

    }

    public void update(Group group) throws Exception {
	verifyNameIsNew(group);
	groupDao.update(group);

    }

    public void delete(int id) throws Exception {
	// проверить, что в группе нет студентов
	verifyIdExists(id);
	groupDao.delete(id);
    }

    private void verifyIdExists(int id) throws Exception {
	if (groupDao.findById(id).isEmpty()) {
	    throw new Exception(String.format("Group with id %s does not exist, nothing to delete", id));
	}
    }

    public List<Group> findAll() {
	return groupDao.findAll();
    }

    public Optional<Group> findById(int id) {
	return groupDao.findById(id);
    }

    private void verifyNameIsNew(Group group) throws Exception {
	if (groupDao.findByName(group.getName()).isPresent()) {
	    throw new Exception(String.format("Group with name %s already exists, can't create/update", group.getName()));
	}
    }
}
