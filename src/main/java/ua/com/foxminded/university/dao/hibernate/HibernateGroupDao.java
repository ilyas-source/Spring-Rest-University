package ua.com.foxminded.university.dao.hibernate;

import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.model.Group;

import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class HibernateGroupDao implements GroupDao {

    private SessionFactory sessionFactory;

    public HibernateGroupDao(SessionFactory sessionFactory) {
        this.sessionFactory=sessionFactory;
    }

    @Override
    public void create(Group entity) {

    }

    @Override
    public Optional<Group> findById(int id) {
        return Optional.empty();
    }

    @Override
    public void update(Group entity) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Group> findAll() {
        return null;
    }

    @Override
    public Optional<Group> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<Group> findByLectureId(int lectureId) {
        return null;
    }
}
