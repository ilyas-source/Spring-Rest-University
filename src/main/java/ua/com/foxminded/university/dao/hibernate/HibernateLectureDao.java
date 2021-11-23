package ua.com.foxminded.university.dao.hibernate;

public class HibernateLectureDao  {

//    private static final Logger logger = LoggerFactory.getLogger(HibernateLectureDao.class);
//
//    private SessionFactory sessionFactory;
//
//    public HibernateLectureDao(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    @Override
//    public void create(Lecture lecture) {
//        Session session = sessionFactory.openSession();
//        session.save(lecture);
//    }
//
//    @Override
//    public Optional<Lecture> findById(int id) {
//        logger.debug("Getting by id: {}", id);
//        Session session = sessionFactory.openSession();
//        return Optional.ofNullable(session.get(Lecture.class, id));
//    }
//
//    @Override
//    public void update(Lecture lecture) {
//        logger.debug("Updating: {}", lecture);
//        Session session = sessionFactory.openSession();
//        session.merge(lecture);
//    }
//
//    @Override
//    public void delete(Lecture lecture) {
//        logger.debug("Deleting: {}", lecture);
//        Session session = sessionFactory.openSession();
//        session.delete(lecture);
//    }
//
//    @Override
//    public List<Lecture> findAll() {
//        logger.debug("Retrieving all lectures from DB");
//        Session session = sessionFactory.openSession();
//        return session.createNamedQuery("SelectAllLectures").list();
//    }
//
//    @Override
//    public List<Lecture> findByClassroom(Classroom classroom) {
//        logger.debug("Searching lectures by classroom: {}", classroom);
//        Session session = sessionFactory.openSession();
//        Query<Lecture> query = session.createNamedQuery("FindLecturesByClassroom")
//                .setParameter("classroom", classroom);
//
//        return query.list();
//    }
//
//    @Override
//    public List<Lecture> findByTeacher(Teacher teacher) {
//        logger.debug("Searching lectures by teacher: {}", teacher);
//        Session session = sessionFactory.openSession();
//        Query<Lecture> query = session.createNamedQuery("FindLecturesByTeacher")
//                .setParameter("teacher", teacher);
//
//        return query.list();
//    }
//
//    @Override
//    public Optional<Lecture> findByDateTimeClassroom(LocalDate date, Timeslot timeslot, Classroom classroom) {
//        logger.debug("Searching for lecture on {} at {} in {}", date, timeslot, classroom);
//        Session session = sessionFactory.openSession();
//        Query<Lecture> query = session.createNamedQuery("FindLectureByDateTimeClassroom")
//                .setParameter("date", date)
//                .setParameter("timeslot", timeslot)
//                .setParameter("classroom", classroom);
//        try {
//            return Optional.of(query.getSingleResult());
//        } catch (NoResultException e) {
//            return Optional.empty();
//        }
//    }
//
//    @Override
//    public List<Lecture> findByDateTime(LocalDate date, Timeslot timeslot) {
//        logger.debug("Searching lectures by date {} and timeslot {}", date, timeslot);
//        Session session = sessionFactory.openSession();
//        Query<Lecture> query = session.createNamedQuery("FindLecturesByDateTime")
//                .setParameter("date", date)
//                .setParameter("timeslot", timeslot);
//
//        return query.list();
//    }
//
//    @Override
//    public Optional<Lecture> findByDateTimeTeacher(LocalDate date, Timeslot timeslot, Teacher teacher) {
//        logger.debug("Searching for lecture on {} at {} by {}", date, timeslot, teacher);
//        Session session = sessionFactory.openSession();
//        Query<Lecture> query = session.createNamedQuery("FindLectureByDateTimeTeacher")
//                .setParameter("date", date)
//                .setParameter("timeslot", timeslot)
//                .setParameter("teacher", teacher);
//        try {
//            return Optional.of(query.getSingleResult());
//        } catch (NoResultException e) {
//            return Optional.empty();
//        }
//    }
//
//    @Override
//    public List<Lecture> findBySubject(Subject subject) {
//        logger.debug("Searching lectures by subject: {}", subject);
//        Session session = sessionFactory.openSession();
//        Query<Lecture> query = session.createNamedQuery("FindLecturesBySubject")
//                .setParameter("subject", subject);
//
//        return query.list();
//    }
//
//    @Override
//    public List<Lecture> findByTimeslot(Timeslot timeslot) {
//        logger.debug("Searching lectures by timeslot: {}", timeslot);
//        Session session = sessionFactory.openSession();
//        Query<Lecture> query = session.createNamedQuery("FindLecturesByTimeslot")
//                .setParameter("timeslot", timeslot);
//
//        return query.list();
//    }
//
//    @Override
//    public List<Lecture> findByTeacherAndPeriod(Teacher teacher, LocalDate startDate, LocalDate endDate) {
//        logger.debug("Searching for lectures for {} on {}-{}", teacher, startDate, endDate);
//        Session session = sessionFactory.openSession();
//        Query<Lecture> query = session.createNamedQuery("FindLecturesByTeacherAndPeriod")
//                .setParameter("teacher", teacher)
//                .setParameter("startDate", startDate)
//                .setParameter("endDate", endDate);
//        return query.list();
//    }
//
//    @Override
//    public List<Lecture> findByStudentAndPeriod(Student student, LocalDate startDate, LocalDate endDate) {
//        logger.debug("Searching for lectures for {} on {}-{}", student, startDate, endDate);
//        Session session = sessionFactory.openSession();
//        String sqlString = "select * from lectures join lectures_groups lg " +
//                " on lectures.id = lg.lecture_id where group_id=:id AND date >= :start AND date<= :end";
//
//        return session.createSQLQuery(sqlString)
//                .addEntity(Lecture.class)
//                .setParameter("id", student.getGroup().getId())
//                .setParameter("start", startDate)
//                .setParameter("end", endDate)
//                .list();
//    }
}








