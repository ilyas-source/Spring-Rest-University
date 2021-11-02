package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.TimeslotDao;
import ua.com.foxminded.university.model.Timeslot;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public class HibernateTimeslotDao implements TimeslotDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateTimeslotDao.class);

    private SessionFactory sessionFactory;

    public HibernateTimeslotDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Timeslot timeslot) {
        Session session = sessionFactory.getCurrentSession();
        session.save(timeslot);
    }

    @Override
    public Optional<Timeslot> findById(int id) {
        logger.debug("Getting by id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Timeslot.class, id));
    }

    @Override
    public void update(Timeslot timeslot) {
        logger.debug("Updating: {}", timeslot);
        Session session = sessionFactory.getCurrentSession();
        session.merge(timeslot);
    }

    @Override
    public void delete(Timeslot timeslot) {
        logger.debug("Deleting: {}", timeslot);
        Session session = sessionFactory.getCurrentSession();
        session.delete(timeslot);
    }

    @Override
    public List<Timeslot> findAll() {
        logger.debug("Retrieving all timeslots from DB");
        Session session = sessionFactory.getCurrentSession();
        return session.createNamedQuery("SelectAllTimeslots").list();
    }

    @Override
    public int countIntersectingTimeslots(Timeslot timeslot) {
        logger.debug("Counting timeslots that intersect {}", timeslot);
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(
                "select count(*) from timeslots where end_time >= :beginTime AND begin_time <=:endTime")
            .setParameter("beginTime", timeslot.getBeginTime())
            .setParameter("endTime", timeslot.getEndTime());

        return ((BigInteger) query.getSingleResult()).intValue();
    }

    @Override
    public Optional<Timeslot> findByBothTimes(Timeslot timeslot) {
        logger.debug("Searching for timeslot {}", timeslot);
        Session session = sessionFactory.getCurrentSession();
        Query<Timeslot> query = session.createNamedQuery("FindTimeslotByBothTimes")
                .setParameter("beginTime", timeslot.getBeginTime())
                .setParameter("endTime", timeslot.getEndTime());
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}








