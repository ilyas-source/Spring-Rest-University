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
	if (groupNameIsNew(group)) {
	    groupDao.create(group);
	} else {
	    throw new Exception(String.format("Group with name %s already exists, can't create", group.getName()));
	}
    }

    public void update(Group group) throws Exception {
	if (groupNameIsNew(group)) {
	    groupDao.update(group);
	} else {
	    throw new Exception(String.format("Group with name %s already exists, can't update", group.getName()));
	}
    }

    public void delete(int id) throws Exception {
	if (idExists(id)) {
	    groupDao.delete(id);
	} else {
	    throw new Exception(String.format("Group with id %s does not exist, nothing to delete", id));
	}
    }

    public List<Group> findAll() {
	return groupDao.findAll();
    }

    public Optional<Group> findById(int id) {
	return groupDao.findById(id);
    }

    private boolean groupNameIsNew(Group group) {
	return groupDao.findByName(group.getName()).isEmpty();
    }

    private boolean idExists(int id) {
	return groupDao.findById(id).isPresent();
    }
}
