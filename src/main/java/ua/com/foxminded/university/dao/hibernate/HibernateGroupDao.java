package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.model.Group;

import java.util.List;
import java.util.Optional;

@Repository
public class HibernateGroupDao implements GroupDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateGroupDao.class);

    private SessionFactory sessionFactory;

    public HibernateGroupDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Group group) {
        Session session = sessionFactory.getCurrentSession();
        session.save(group);
    }

    @Override
     public Optional<Group> findById(int id) {
        logger.debug("Getting by id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Group.class, id));
    }

    @Override
    public void update(Group group) {
        logger.debug("Updating: {}", group);
        Session session = sessionFactory.getCurrentSession();
        session.merge(group);
    }

    @Override
    public void delete(Group group) {
        logger.debug("Deleting: {}", group);
        Session session = sessionFactory.getCurrentSession();
        session.delete(group);
    }

    @Override
    public List<Group> findAll() {
        logger.debug("Retrieving all groups from DB");
        Session session = sessionFactory.getCurrentSession();
        return session.createNamedQuery("SelectAllGroups").list();
    }

    @Override
    public Optional<Group> findByName(String name) {
        logger.debug("Searching group by name: {}", name);
        Session session = sessionFactory.getCurrentSession();
        var query = session.createNamedQuery("FindByName");
        query.setParameter("name", name);

        return Optional.ofNullable(session.get(Group.class, name));
    }

    @Override
    public List<Group> findByLectureId(int lectureId) {
        logger.debug("Searching by lecture id: {}", lectureId);
        Session session = sessionFactory.getCurrentSession();
        var query = session.createNamedQuery("FindByLectureId");
        query.setParameter("id", lectureId);
        return query.list();
    }
}
