package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.exception.GroupNotEmptyException;
import ua.com.foxminded.university.model.Group;

@Service
public class GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private GroupDao groupDao;
    private StudentDao studentDao;

    public GroupService(GroupDao groupDao, StudentDao studentDao) {
	this.groupDao = groupDao;
	this.studentDao = studentDao;
    }

    public void create(Group group) {
	logger.debug("Creating a new group: {} ", group);
	verifyNameIsUnique(group);
	groupDao.create(group);
    }

    public List<Group> findAll() {
	return groupDao.findAll();
    }

    public Optional<Group> findById(int id) {
	return groupDao.findById(id);
    }

    public void update(Group group) {
	logger.debug("Updating group: {} ", group);
	verifyNameIsUnique(group);
	groupDao.update(group);
    }

    public void delete(int id) {
	logger.debug("Deleting group by id: {} ", id);
	Optional<Group> group = groupDao.findById(id);
	if (group.isEmpty()) {
	    throw new EntityNotFoundException(String.format("Group with id:%s not found, nothing to delete", id));
	}
	verifyHasNoStudents(group.get());
	groupDao.delete(id);
    }

    private void verifyNameIsUnique(Group group) {
	Optional<Group> groupByName = groupDao.findByName(group.getName());
	if ((groupByName.isPresent() && (groupByName.get().getId() != group.getId()))) {
	    throw new EntityNotUniqueException(String.format("Group with name %s already exists", group.getName()));
	}
    }

    private void verifyHasNoStudents(Group group) {
	if (!studentDao.findByGroup(group).isEmpty()) {
	    throw new GroupNotEmptyException("Group has assigned students, can't delete");
	}
    }

}
