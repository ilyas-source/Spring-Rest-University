package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.exception.GroupNotEmptyException;
import ua.com.foxminded.university.model.Group;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
        logger.debug("Retrieving all groups from DAO");
        return groupDao.findAll();
    }

    public Optional<Group> findById(int id) {
        return groupDao.findById(id);
    }

    public Group getById(int id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find group by id " + id));
    }

    public void update(Group group) {
        logger.debug("Updating group: {} ", group);
        verifyNameIsUnique(group);
        groupDao.update(group);
    }

    public void delete(int id) {
        logger.debug("Deleting group by id: {} ", id);
        Group group=getById(id);
        verifyHasNoStudents(group);
        groupDao.delete(group);
    }

    private void verifyNameIsUnique(Group group) {
        groupDao.findByName(group.getName())
                .filter(s -> s.getId() != group.getId())
                .ifPresent(s -> {
                    throw new EntityNotUniqueException(String.format("Group %s already exists", s.getName()));
                });
    }

    private void verifyHasNoStudents(Group group) {
        if (!studentDao.findByGroup(group).isEmpty()) {
            throw new GroupNotEmptyException(String.format("Group %s has assigned students, can't delete", group.getName()));
        }
    }
}
