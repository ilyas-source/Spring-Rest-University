package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;
import java.util.Optional;

@Repository
public class HibernateTeacherDao implements TeacherDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateTeacherDao.class);

    private SessionFactory sessionFactory;

    public HibernateTeacherDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Teacher teacher) {
        Session session = sessionFactory.getCurrentSession();
        session.save(teacher);
    }

    @Override
    public Optional<Teacher> findById(int id) {
        logger.debug("Getting by id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Teacher.class, id));
    }

    @Override
    public void update(Teacher teacher) {
        logger.debug("Updating: {}", teacher);
        Session session = sessionFactory.getCurrentSession();
        session.merge(teacher);
    }

    @Override
    public void delete(Teacher teacher) {
        logger.debug("Deleting: {}", teacher);
        Session session = sessionFactory.getCurrentSession();
        session.delete(teacher);
    }

    @Override
    public List<Teacher> findAll() {
        logger.debug("Retrieving all teachers from DB");
        Session session = sessionFactory.getCurrentSession();
        return session.createNamedQuery("SelectAllTeachers").list();
    }

    @Override
    public Page<Teacher> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Teacher> findByAddressId(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Teacher> findByNameAndEmail(String firstName, String lastName, String email) {
        return Optional.empty();
    }

    @Override
    public List<Teacher> findBySubstring(String substring) {
        return null;
    }

    @Override
    public List<Teacher> getReplacementCandidates(Lecture lecture) {
        return null;
    }
}








