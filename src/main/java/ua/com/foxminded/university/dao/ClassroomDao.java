package ua.com.foxminded.university.dao;

import java.util.Optional;

import ua.com.foxminded.university.model.Classroom;

public interface ClassroomDao extends GeneralDao<Classroom> {

    Optional<Classroom> findByName(String name);
}
