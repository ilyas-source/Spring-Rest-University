package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.SubjectDao;
import ua.com.foxminded.university.model.Subject;

import java.util.List;
import java.util.Optional;

@Repository
public class HibernateSubjectDao implements SubjectDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateSubjectDao.class);

    private SessionFactory sessionFactory;

    public HibernateSubjectDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Subject subject) {
        Session session = sessionFactory.getCurrentSession();
        session.save(subject);
    }

    @Override
    public Optional<Subject> findById(int id) {
        logger.debug("Getting by id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Subject.class, id));
    }

    @Override
    public void update(Subject subject) {
        logger.debug("Updating: {}", subject);
        Session session = sessionFactory.getCurrentSession();
        session.merge(subject);
    }

    @Override
    public void delete(Subject subject) {
        logger.debug("Deleting: {}", subject);
        Session session = sessionFactory.getCurrentSession();
        session.delete(subject);
    }

    @Override
    public List<Subject> findAll() {
        logger.debug("Retrieving all subjects from DB");
        Session session = sessionFactory.getCurrentSession();
        return session.createNamedQuery("SelectAllSubjects").list();
    }

    @Override
    public List<Subject> getByTeacherId(int id) {
        return null;
    }

    @Override
    public Optional<Subject> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public int countAssignments(Subject subject) {
        return 0;
    }
}








