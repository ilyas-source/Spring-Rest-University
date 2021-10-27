package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.model.Group;

import java.util.List;
import java.util.Optional;

@Component
public class HibernateGroupDao implements GroupDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateGroupDao.class);

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
    @Transactional
    public List<Group> findAll() {
        logger.debug("Retrieving all groups from DB");
        Session session = sessionFactory.getCurrentSession();

        return session.createNamedQuery("SelectAllGroups").list();
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
