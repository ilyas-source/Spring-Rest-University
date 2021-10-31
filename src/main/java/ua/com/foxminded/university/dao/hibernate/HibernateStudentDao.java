package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class HibernateStudentDao implements StudentDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateStudentDao.class);

    private SessionFactory sessionFactory;

    public HibernateStudentDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Student student) {
        Session session = sessionFactory.getCurrentSession();
        session.save(student);
    }

    @Override
    public Optional<Student> findById(int id) {
        logger.debug("Getting by id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Student.class, id));
    }

    @Override
    public void update(Student student) {
        logger.debug("Updating: {}", student);
        Session session = sessionFactory.getCurrentSession();
        session.merge(student);
    }

    @Override
    public void delete(Student student) {
        logger.debug("Deleting: {}", student);
        Session session = sessionFactory.getCurrentSession();
        session.delete(student);
    }

    @Override
    public List<Student> findAll() {
        logger.debug("Retrieving all students from DB");
        Session session = sessionFactory.getCurrentSession();
        return session.createNamedQuery("SelectAllStudents").list();
    }

    @Override
    public int countInGroup(Group group) {
        return 0;
    }

    @Override
    public Optional<Student> findByAddressId(int id) {
        return Optional.empty();
    }

    @Override
    public List<Student> findByGroup(Group group) {
        return null;
    }

    @Override
    public Optional<Student> findByNameAndBirthDate(String firstName, String lastName, LocalDate birthDate) {
        return Optional.empty();
    }

    @Override
    public Page<Student> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Student> findBySubstring(String substring) {
        return null;
    }
}








