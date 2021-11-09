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
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Teacher;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public class HibernateTeacherDao implements TeacherDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateTeacherDao.class);

    @Value("${student.defaultsortattribute}")
    private String defaultSortAttribute;

    @Value("${defaultsortdirection}")
    private String defaultSortDirection;

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
        List<Teacher> result = session.createNamedQuery("SelectAllTeachers").list();

        return result;
    }

    @Override
    public Page<Teacher> findAll(Pageable pageable) {
        logger.debug("Retrieving Teachers pageable");
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
        String request = String.format("from Teacher order by %s %s", sortProperty, sortDirection);
        List<Teacher> teachers = session.createQuery(request)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .list();
        Query query = session.createSQLQuery("select count(*) from teachers");
        var totalTeachers = ((BigInteger) query.getSingleResult()).longValue();

        return new PageImpl<>(teachers, pageable, totalTeachers);
    }

    @Override
    public Optional<Teacher> findByNameAndEmail(String firstName, String lastName, String email) {
        logger.debug("Looking for {} {} with email {}", lastName, firstName, email);
        Session session = sessionFactory.getCurrentSession();
        Query<Teacher> query = session.createNamedQuery("findTeacherByNameAndEmail")
                .setParameter("lastName", lastName)
                .setParameter("firstName", firstName)
                .setParameter("email", email);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Teacher> findBySubstring(String substring) {
        logger.debug("Searching teachers by substring: {}", substring);
        Session session = sessionFactory.getCurrentSession();
        String formattedSubstring = "%" + substring.toLowerCase() + "%";
        String sqlString = "SELECT * FROM teachers WHERE lower(concat(first_name,' ',last_name)) like :substring";
        return session.createSQLQuery(sqlString)
                .setParameter("substring", formattedSubstring)
                .addEntity(Teacher.class)
                .list();
    }

    @Override
    public List<Teacher> getReplacementCandidates(Lecture lecture) {
        String sqlQuery = "SELECT * FROM teachers JOIN teachers_subjects ts " +
                "ON teachers.id = ts.teacher_id WHERE subject_id= :subject_id AND teacher_id!= :teacher_id";
        Session session = sessionFactory.getCurrentSession();
        return session.createSQLQuery(sqlQuery)
                .addEntity(Teacher.class)
                .setParameter("subject_id", lecture.getSubject().getId())
                .setParameter("teacher_id", lecture.getTeacher().getId())
                .list();
    }
}








