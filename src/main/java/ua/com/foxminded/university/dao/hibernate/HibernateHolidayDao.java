package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.model.Holiday;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class HibernateHolidayDao implements HolidayDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateHolidayDao.class);

    private SessionFactory sessionFactory;

    public HibernateHolidayDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Holiday holiday) {
        Session session = sessionFactory.openSession();
        session.save(holiday);
    }

    @Override
    public Optional<Holiday> findById(int id) {
        logger.debug("Getting by id: {}", id);
        Session session = sessionFactory.openSession();
        return Optional.ofNullable(session.get(Holiday.class, id));
    }

    @Override
    public void update(Holiday holiday) {
        logger.debug("Updating: {}", holiday);
        Session session = sessionFactory.openSession();
        session.merge(holiday);
    }

    @Override
    public void delete(Holiday holiday) {
        logger.debug("Deleting: {}", holiday);
        Session session = sessionFactory.openSession();
        session.delete(holiday);
    }

    @Override
    public List<Holiday> findAll() {
        logger.debug("Retrieving all holidays from DB");
        Session session = sessionFactory.openSession();
        return session.createNamedQuery("SelectAllHolidays").list();
    }

    @Override
    public List<Holiday> findByDate(LocalDate date) {
        logger.debug("Searching holiday by date: {}", date);
        Session session = sessionFactory.openSession();
        Query<Holiday> query = session.createNamedQuery("FindByDate")
                .setParameter("date", date);
        return query.list();
    }
}





