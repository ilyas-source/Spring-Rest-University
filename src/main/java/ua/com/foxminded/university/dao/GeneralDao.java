package ua.com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

public interface GeneralDao<T> {

    void create(T object);

    Optional<T> findById(int id);

    List<T> findAll();

    void update(T object);

    void delete(int id);
}
