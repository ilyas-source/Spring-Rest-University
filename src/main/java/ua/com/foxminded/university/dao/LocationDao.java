package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Location;

import java.util.List;

public interface LocationDao extends GeneralDao<Location> {

    List<Location> findAll();
}
