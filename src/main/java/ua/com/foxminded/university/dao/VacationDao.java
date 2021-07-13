package ua.com.foxminded.university.dao;

import java.util.List;

import ua.com.foxminded.university.model.Vacation;

public interface VacationDao extends GeneralDao<Vacation> {

    List<Vacation> findByTeacherId(int id);
}
