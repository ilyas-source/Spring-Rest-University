package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Subject;

import java.util.Optional;

public interface SubjectDao extends GeneralDao<Subject> {

    Optional<Subject> findByName(String name);

    long countAssignments(Subject subject);
}
