package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class HibernateStudentDao implements StudentDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateStudentDao.class);

    @Value("${student.defaultsortattribute}")
    private String defaultSortAttribute;

    @Value("${defaultsortdirection}")
    private String defaultSortDirection;

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
        logger.debug("Counting students in {}", group);
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(
                "select count(*) from students where group_id=:id");
        query.setParameter("id", group.getId());

        return ((BigInteger) query.getSingleResult()).intValue();
    }

    @Override
    public List<Student> findByGroup(Group group) {
        logger.debug("Searching students by group: {}", group);
        Session session = sessionFactory.getCurrentSession();
        Query<Student> query = session.createNamedQuery("FindStudentByGroup")
                .setParameter("group", group);

        return query.list();
    }

    @Override
    public Optional<Student> findByNameAndBirthDate(String firstName, String lastName, LocalDate birthDate) {
        logger.debug("Searching for {} {}, born {}", firstName, lastName, birthDate);
        Session session = sessionFactory.getCurrentSession();
        Query<Student> query = session.createNamedQuery("findStudentByNameAndBirthDate")
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .setParameter("birthDate", birthDate);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Student> findAll(Pageable pageable) {
        var sortProperty = defaultSortAttribute;
        var sortDirection = Sort.Direction.fromString(defaultSortDirection);

        var sortOrder = pageable.getSort().get().findFirst();
        if (sortOrder.isPresent()) {
            sortProperty = sortOrder.get().getProperty();
            sortDirection = sortOrder.get().getDirection();
        }
        int offset = Math.toIntExact(pageable.getOffset());
        int pageSize = pageable.getPageSize();

        logger.debug("Retrieving offset {}, size {}, sort {}", offset, pageSize, pageable.getSort());

        Session session = sessionFactory.getCurrentSession();
        var students = session.createNamedQuery("selectAllStudents")
                .setFirstResult(offset)
                .setFetchSize(pageSize)
                .list();
        Query query = session.createSQLQuery("select count(*) from students");
        var totalStudents = ((BigInteger) query.getSingleResult()).longValue();

        return new PageImpl<>(students, pageable, totalStudents);
    }

    @Override
    public List<Student> findBySubstring(String substring) {
        logger.debug("Searching students by substring: {}", substring);
        Session session = sessionFactory.getCurrentSession();
        String formattedSubstring = "%" + substring.toLowerCase() + "%";
        String sqlString = "SELECT * FROM students WHERE lower(concat(first_name,' ',last_name)) like :substring";
        return session.createSQLQuery(sqlString)
                .setParameter("substring", formattedSubstring)
                .addEntity(Student.class)
                .list();
    }
}








