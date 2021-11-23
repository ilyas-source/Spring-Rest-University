package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.Teacher;
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
        Session session = sessionFactory.openSession();
        session.save(vacation);
    }

    @Override
    public Optional<Vacation> findById(int id) {
        logger.debug("Getting by id: {}", id);
        Session session = sessionFactory.openSession();
        return Optional.ofNullable(session.get(Vacation.class, id));
    }

    @Override
    public void update(Vacation vacation) {
        logger.debug("Updating: {}", vacation);
        Session session = sessionFactory.openSession();
        session.merge(vacation);
    }

    @Override
    public void delete(Vacation vacation) {
        logger.debug("Deleting: {}", vacation);
        Session session = sessionFactory.openSession();
        session.delete(vacation);
    }

    @Override
    public List<Vacation> findAll() {
        logger.debug("Retrieving all vacations from DB");
        Session session = sessionFactory.openSession();
        return session.createNamedQuery("SelectAllVacations").list();
    }

    @Override
    public List<Vacation> findByTeacher(Teacher teacher) {
        logger.debug("Searching vacations by teacher: {}", teacher);
        Session session = sessionFactory.openSession();
        Query<Vacation> query = session.createNamedQuery("FindVacationsByTeacher")
                .setParameter("teacher", teacher);

        return query.list();
    }
}








