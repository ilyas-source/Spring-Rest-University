package ua.com.foxminded.university.dao;

import java.util.Optional;

import ua.com.foxminded.university.model.Teacher;

public interface TeacherDao extends GeneralDao<Teacher> {

    Optional<Teacher> findByAddressId(int id);

}
