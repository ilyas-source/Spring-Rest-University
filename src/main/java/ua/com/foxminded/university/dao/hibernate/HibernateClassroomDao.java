package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;

import java.util.List;
import java.util.Optional;

@Repository
public class HibernateClassroomDao implements ClassroomDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateClassroomDao.class);

    private SessionFactory sessionFactory;

    public HibernateClassroomDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Classroom classroom) {
        Session session = sessionFactory.getCurrentSession();
        session.save(classroom);
    }

    @Override
    public Optional<Classroom> findById(int id) {
        logger.debug("Getting by id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Classroom.class, id));
    }

    @Override
    public void update(Classroom classroom) {
        logger.debug("Updating: {}", classroom);
        Session session = sessionFactory.getCurrentSession();
        session.merge(classroom);
    }

    @Override
    public void delete(Classroom classroom) {
        logger.debug("Deleting: {}", classroom);
        Session session = sessionFactory.getCurrentSession();
        session.delete(classroom);
    }

    @Override
    public List<Classroom> findAll() {
        logger.debug("Retrieving all classrooms from DB");
        Session session = sessionFactory.getCurrentSession();
        return session.createNamedQuery("SelectAllClassrooms").list();
    }

    @Override
    public Optional<Classroom> findByName(String name) { // TODO
        return Optional.empty();
    }

    @Override
    public Optional<Classroom> findByLocation(Location location) { // TODO
        return Optional.empty();
    }
}








