package ua.com.foxminded.university.dao;

import java.util.Optional;

import ua.com.foxminded.university.model.Group;

public interface GroupDao extends GeneralDao<Group> {

    Optional<Group> findByName(String name);

}
