package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Group;

import java.util.Optional;

public interface GroupDao extends GeneralDao<Group> {

    Optional<Group> findByName(String name);
}
