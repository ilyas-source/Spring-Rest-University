package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.SubjectDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
public class HibernateSubjectDao implements SubjectDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateSubjectDao.class);

    private TeacherDao teacherDao;
    private SessionFactory sessionFactory;

    public HibernateSubjectDao(SessionFactory sessionFactory, TeacherDao teacherDao) {
        this.sessionFactory = sessionFactory;
        this.teacherDao=teacherDao;
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
    public Optional<Subject> findByName(String name) {
        logger.debug("Searching subject by name: {}", name);
        Session session = sessionFactory.getCurrentSession();
        Query<Subject> query = session.createNamedQuery("FindSubjectByName")
                .setParameter("name", name);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Subject> getByTeacherId(int id) {
        logger.debug("Retrieving subjects by teacher id: {}", id);
        Optional<Teacher> optionalTeacher = teacherDao.findById(id);
        Teacher teacher=new Teacher();
        if(optionalTeacher.isPresent()) {
            teacher=optionalTeacher.get();
        }

        Session session = sessionFactory.getCurrentSession();
        return session.createNamedQuery("FindSubjectsByTeacher")
                .setParameter("teacher", teacher)
                .list();
    }

    @Override
    public int countAssignments(Subject subject) {
        return 0;
    }
}








