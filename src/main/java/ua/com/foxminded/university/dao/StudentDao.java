package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

public interface StudentDao extends GeneralDao<Student> {
    public int countStudentsInGroup(Group group);
}
