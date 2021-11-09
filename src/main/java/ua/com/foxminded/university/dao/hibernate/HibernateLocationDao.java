package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.LocationDao;
import ua.com.foxminded.university.model.Location;

import java.util.List;
import java.util.Optional;

@Repository
public class HibernateLocationDao implements LocationDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateLocationDao.class);

    private SessionFactory sessionFactory;

    public HibernateLocationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Location location) {
        Session session = sessionFactory.getCurrentSession();
        session.save(location);
    }

    @Override
    public Optional<Location> findById(int id) {
        logger.debug("Getting by id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Location.class, id));
    }

    @Override
    public void update(Location location) {
        logger.debug("Updating: {}", location);
        Session session = sessionFactory.getCurrentSession();
        session.merge(location);
    }

    @Override
    public void delete(Location location) {
        logger.debug("Deleting: {}", location);
        Session session = sessionFactory.getCurrentSession();
        session.delete(location);
    }

    @Override
    public List<Location> findAll() {
        logger.debug("Retrieving all locations from DB");
        Session session = sessionFactory.getCurrentSession();
        return session.createNamedQuery("SelectAllLocations").list();
    }
}








