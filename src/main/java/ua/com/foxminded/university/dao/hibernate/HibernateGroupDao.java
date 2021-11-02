package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.model.Group;

import javax.persistence.NoResultException;
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
        Query<Group> query = session.createNamedQuery("FindGroupByName")
                            .setParameter("name", name);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Group> findByLectureId(int lectureId) {
        logger.debug("Searching by lecture id: {}", lectureId);
        Session session = sessionFactory.getCurrentSession();
        String sqlString = "SELECT g.id, g.name from lectures_groups AS l_g LEFT JOIN groups AS g " +
        "ON (l_g.group_id=g.id) WHERE l_g.lecture_id = :lectureId";

        return session.createSQLQuery(sqlString)
                        .addEntity(Group.class)
                        .setParameter("lectureId", lectureId)
                        .list();
    }
}
