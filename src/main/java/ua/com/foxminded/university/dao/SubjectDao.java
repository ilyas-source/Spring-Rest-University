package ua.com.foxminded.university.dao;

import java.util.List;

import ua.com.foxminded.university.model.Subject;

public interface SubjectDao extends GeneralDao<Subject> {

    public List<Subject> getSubjectsByTeacherId(int id);
}
