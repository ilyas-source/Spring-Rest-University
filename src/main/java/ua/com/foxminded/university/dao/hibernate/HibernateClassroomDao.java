package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

//@Repository
public class HibernateClassroomDao implements ClassroomDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateClassroomDao.class);

    private SessionFactory sessionFactory;

    public HibernateClassroomDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Classroom classroom) {
        Session session = sessionFactory.openSession();
        session.save(classroom);
    }

    @Override
    public Optional<Classroom> findById(int id) {
        logger.debug("Getting by id: {}", id);
        Session session = sessionFactory.openSession();
        return Optional.ofNullable(session.get(Classroom.class, id));
    }

    @Override
    public void update(Classroom classroom) {
        logger.debug("Updating: {}", classroom);
        Session session = sessionFactory.openSession();
        session.merge(classroom);
    }

    @Override
    public void delete(Classroom classroom) {
        logger.debug("Deleting: {}", classroom);
        Session session = sessionFactory.openSession();
        session.delete(classroom);
    }

    @Override
    public List<Classroom> findAll() {
        logger.debug("Retrieving all classrooms from DB");
        Session session = sessionFactory.openSession();
        return session.createNamedQuery("SelectAllClassrooms").list();
    }

    @Override
    public Optional<Classroom> findByName(String name) {
        logger.debug("Searching classroom by name: {}", name);
        Session session = sessionFactory.openSession();
        Query<Classroom> query = session.createNamedQuery("FindClassroomByName")
                .setParameter("name", name);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Classroom> findByLocation(Location location) {
        logger.debug("Searching classroom by location: {}", location);
        Session session = sessionFactory.openSession();
        Query<Classroom> query = session.createNamedQuery("FindClassroomByLocation")
                .setParameter("location", location);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}








