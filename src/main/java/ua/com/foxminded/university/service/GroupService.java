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
	if (isUniqueName(group)) {
	    groupDao.create(group);
	}
    }

    public void update(Group group) {
	if (isUniqueName(group))
	    groupDao.update(group);
    }

    private boolean isUniqueName(Group group) {
	Optional<Group> groupByName = groupDao.findByName(group.getName());
	return !(groupByName.isPresent()
		&& (groupByName.get().getId() != group.getId()));
    }

    public void delete(int id) {
	Optional<Group> group = groupDao.findById(id);
	var canDelete = group.isPresent() && isEmpty(group.get());
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
}
