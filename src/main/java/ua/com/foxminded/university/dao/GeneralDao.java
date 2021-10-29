package ua.com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

public interface GeneralDao<T> {

    void create(T entity);

    Optional<T> findById(int id);

    void update(T entity);

    void delete(T entity);

    List<T> findAll();
}
