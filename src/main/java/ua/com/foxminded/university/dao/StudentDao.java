package ua.com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

public interface StudentDao extends GeneralDao<Student> {

    int countInGroup(Group group);

    Optional<Student> findByAddressId(int id);

    List<Student> findByGroup(Group group);
}
