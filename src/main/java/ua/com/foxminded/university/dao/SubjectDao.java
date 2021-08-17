package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectDao extends GeneralDao<Subject> {

    List<Subject> getByTeacherId(int id);

    Optional<Subject> findByName(String name);

    int countAssignments(Subject subject);
}
