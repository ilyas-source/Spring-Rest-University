package ua.com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

public interface GeneralDao<T> {

    void create(T entity);

    Optional<T> findById(int id);

    List<T> findAll();

    void update(T entity);

    void delete(int id);
}
