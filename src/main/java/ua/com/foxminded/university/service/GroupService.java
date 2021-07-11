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

    public GroupService(GroupDao GroupDao, StudentDao studentDao) {
	this.groupDao = GroupDao;
	this.studentDao = studentDao;
    }

    public void create(Group group) {
	if (nameIsNew(group)) {
	    groupDao.create(group);
	} else {
	    System.out.println("Group with this name already exists, can't create new");
	}
    }

    public void update(Group group) {
	if (nameIsNew(group)) {
	    groupDao.update(group);
	} else {
	    System.out.println("Group with this name already exists, can't update");
	}
    }

    public void delete(int id) {
	boolean canDelete = idExists(id) && isEmpty(groupDao.findById(id).get());
	if (canDelete) {
	    groupDao.delete(id);
	} else {
	    System.out.println("Can't delete group");
	}
    }

    private boolean isEmpty(Group group) {
	return studentDao.findByGroup(group).isEmpty();
    }

    private boolean idExists(int id) {
	return groupDao.findById(id).isPresent();
    }

    public List<Group> findAll() {
	return groupDao.findAll();
    }

    public Optional<Group> findById(int id) {
	return groupDao.findById(id);
    }

    private boolean nameIsNew(Group group) {
	return groupDao.findByName(group.getName()).isEmpty();
    }
}
