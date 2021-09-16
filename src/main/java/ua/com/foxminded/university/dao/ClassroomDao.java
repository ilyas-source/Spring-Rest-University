package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;

import java.util.List;
import java.util.Optional;

public interface ClassroomDao extends GeneralDao<Classroom> {

    List<Classroom> findAll();

    Optional<Classroom> findByName(String name);

    Optional<Classroom> findByLocation(Location location);
}
