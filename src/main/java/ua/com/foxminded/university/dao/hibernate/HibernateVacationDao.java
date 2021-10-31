package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.Vacation;

import java.util.List;
import java.util.Optional;

@Repository
public class HibernateVacationDao implements VacationDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateVacationDao.class);

    private SessionFactory sessionFactory;

    public HibernateVacationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Vacation vacation) {
        Session session = sessionFactory.getCurrentSession();
        session.save(vacation);
    }

    @Override
    public Optional<Vacation> findById(int id) {
        logger.debug("Getting by id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Vacation.class, id));
    }

    @Override
    public void update(Vacation vacation) {
        logger.debug("Updating: {}", vacation);
        Session session = sessionFactory.getCurrentSession();
        session.merge(vacation);
    }

    @Override
    public void delete(Vacation vacation) {
        logger.debug("Deleting: {}", vacation);
        Session session = sessionFactory.getCurrentSession();
        session.delete(vacation);
    }

    @Override
    public List<Vacation> findAll() {
        logger.debug("Retrieving all vacations from DB");
        Session session = sessionFactory.getCurrentSession();
        return session.createNamedQuery("SelectAllVacations").list();
    }

    @Override
    public List<Vacation> findByTeacherId(int id) { // TODO
        return null;
    }
}








