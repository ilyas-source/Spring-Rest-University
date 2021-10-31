package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class HibernateLectureDao implements LectureDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateLectureDao.class);

    private SessionFactory sessionFactory;

    public HibernateLectureDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Lecture lecture) {
        Session session = sessionFactory.getCurrentSession();
        session.save(lecture);
    }

    @Override
    public Optional<Lecture> findById(int id) {
        logger.debug("Getting by id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Lecture.class, id));
    }

    @Override
    public void update(Lecture lecture) {
        logger.debug("Updating: {}", lecture);
        Session session = sessionFactory.getCurrentSession();
        session.merge(lecture);
    }

    @Override
    public void delete(Lecture lecture) {
        logger.debug("Deleting: {}", lecture);
        Session session = sessionFactory.getCurrentSession();
        session.delete(lecture);
    }

    @Override
    public List<Lecture> findAll() {
        logger.debug("Retrieving all lectures from DB");
        Session session = sessionFactory.getCurrentSession();
        return session.createNamedQuery("SelectAllLectures").list();
    }

    @Override
    public List<Lecture> findByClassroom(Classroom classroom) { // TODO TODO TODO
        return null;
    }

    @Override
    public List<Lecture> findByTeacher(Teacher teacher) {
        return null;
    }

    @Override
    public Optional<Lecture> findByDateTimeClassroom(LocalDate date, Timeslot timeslot, Classroom classroom) {
        return Optional.empty();
    }

    @Override
    public List<Lecture> findByDateTime(LocalDate date, Timeslot timeslot) {
        return null;
    }

    @Override
    public Optional<Lecture> findByDateTimeTeacher(LocalDate date, Timeslot timeslot, Teacher teacher) {
        return Optional.empty();
    }

    @Override
    public List<Lecture> findBySubject(Subject subject) {
        return null;
    }

    @Override
    public List<Lecture> findByTimeslot(Timeslot timeslot) {
        return null;
    }

    @Override
    public List<Lecture> findByTeacherAndPeriod(Teacher teacher, LocalDate start, LocalDate end) {
        return null;
    }

    @Override
    public List<Lecture> findByStudentAndPeriod(Student student, LocalDate start, LocalDate end) {
        return null;
    }
}








