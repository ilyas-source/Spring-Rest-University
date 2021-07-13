package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Group;

@Service
public class GroupService {

    private GroupDao groupDao;
    private StudentDao studentDao;

    public GroupService(GroupDao groupDao, StudentDao studentDao) {
	this.groupDao = groupDao;
	this.studentDao = studentDao;
    }

    public void create(Group group) {
	if (hasNewName(group)) {
	    groupDao.create(group);
	}
    }

    public void update(Group group) {
	if (hasNewName(group)) {
	    groupDao.update(group);
	}
    }

    public void delete(int id) {
	Optional<Group> optionalGroup = groupDao.findById(id);
	boolean canDelete = optionalGroup.isPresent() && isEmpty(optionalGroup.get());
	if (canDelete) {
	    groupDao.delete(id);
	}
    }

    private boolean isEmpty(Group group) {
	return studentDao.findByGroup(group).isEmpty();
    }

    public List<Group> findAll() {
	return groupDao.findAll();
    }

    public Optional<Group> findById(int id) {
	return groupDao.findById(id);
    }

    private boolean hasNewName(Group group) {
	return groupDao.findByName(group.getName()).isEmpty();
    }
}
