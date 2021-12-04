package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.EntityNotUniqueException;
import ua.com.foxminded.university.exception.GroupNotEmptyException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private GroupRepository groupRepository;
    private StudentRepository studentRepository;

    public GroupService(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    public void create(Group group) {
        logger.debug("Creating a new group: {} ", group);
        verifyNameIsUnique(group);
        group.setId(0);
        groupRepository.save(group);
    }

    public List<Group> findAll() {
        logger.debug("Retrieving all groups from DAO");
        return groupRepository.findAll();
    }

    public Optional<Group> findById(int id) {
        return groupRepository.findById(id);
    }

    public Group getById(int id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find group by id " + id));
    }

    public void update(Group group) {
        logger.debug("Updating group: {} ", group);
        verifyNameIsUnique(group);
        groupRepository.save(group);
    }

    public void delete(int id) {
        logger.debug("Deleting group by id: {} ", id);
        Group group = getById(id);
        verifyHasNoStudents(group);
        groupRepository.delete(group);
    }

    private void verifyNameIsUnique(Group group) {
        groupRepository.findByName(group.getName())
                .filter(s -> s.getId() != group.getId())
                .ifPresent(s -> {
                    throw new EntityNotUniqueException(String.format("Group %s already exists", s.getName()));
                });
    }

    private void verifyHasNoStudents(Group group) {
        if (!studentRepository.findByGroup(group).isEmpty()) {
            throw new GroupNotEmptyException(String.format("Group %s has assigned students, can't delete", group.getName()));
        }
    }
}
