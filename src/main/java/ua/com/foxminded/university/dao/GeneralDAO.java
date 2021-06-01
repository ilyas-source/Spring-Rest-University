package ua.com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

public interface GeneralDAO<E> {

    void create(E e);

    Optional<E> findById(int id);

    List<E> findAll();

    void update(E e);

    void delete(int id);
}
