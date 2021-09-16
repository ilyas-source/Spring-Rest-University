package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao extends GeneralDao<Group> {

    List<Group> findAll();

    Optional<Group> findByName(String name);

    List<Group> findByLectureId(int lectureId);
}
