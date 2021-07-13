package ua.com.foxminded.university.dao;

import java.util.Optional;

import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;

public interface ClassroomDao extends GeneralDao<Classroom> {

    Optional<Classroom> findByName(String name);

    Optional<Classroom> findByLocation(Location location);

    Optional<Classroom> findByNameAndId(String name, int id);
}
