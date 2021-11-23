package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

import java.util.List;

public interface VacationDao extends GeneralDao<Vacation> {

    List<Vacation> findByTeacher(Teacher teacher);
}
