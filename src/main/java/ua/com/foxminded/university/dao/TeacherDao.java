package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Teacher;

import java.util.Optional;

public interface TeacherDao extends GeneralDao<Teacher> {

    Optional<Teacher> findByAddressId(int id);

    Optional<Teacher> findByNameAndEmail(String firstName, String lastName, String email);

}
