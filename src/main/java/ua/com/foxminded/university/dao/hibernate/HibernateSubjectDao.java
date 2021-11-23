package ua.com.foxminded.university.dao.hibernate;

public class HibernateSubjectDao {

//    private static final Logger logger = LoggerFactory.getLogger(HibernateSubjectDao.class);
//
//    private SessionFactory sessionFactory;
//
//    public HibernateSubjectDao(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    @Override
//    public void create(Subject subject) {
//        Session session = sessionFactory.openSession();
//        session.save(subject);
//    }
//
//    @Override
//    public Optional<Subject> findById(int id) {
//        logger.debug("Getting by id: {}", id);
//        Session session = sessionFactory.openSession();
//        return Optional.ofNullable(session.get(Subject.class, id));
//    }
//
//    @Override
//    public void update(Subject subject) {
//        logger.debug("Updating: {}", subject);
//        Session session = sessionFactory.openSession();
//        session.merge(subject);
//    }
//
//    @Override
//    public void delete(Subject subject) {
//        logger.debug("Deleting: {}", subject);
//        Session session = sessionFactory.openSession();
//        session.delete(subject);
//    }
//
//    @Override
//    public List<Subject> findAll() {
//        logger.debug("Retrieving all subjects from DB");
//        Session session = sessionFactory.openSession();
//        return session.createNamedQuery("SelectAllSubjects").list();
//    }
//
//    @Override
//    public Optional<Subject> findByName(String name) {
//        logger.debug("Searching subject by name: {}", name);
//        Session session = sessionFactory.openSession();
//        Query<Subject> query = session.createNamedQuery("FindSubjectByName")
//                .setParameter("name", name);
//        try {
//            return Optional.of(query.getSingleResult());
//        } catch (NoResultException e) {
//            return Optional.empty();
//        }
//    }
//
//    @Override
//    public long countAssignments(Subject subject) {
//        logger.debug("Counting assignments to teachers: {}", subject);
//        Session session = sessionFactory.openSession();
//        Query query = session.createQuery("select count(*) from Teacher where :subject in elements(subjects)")
//                .setParameter("subject", subject);
//        return (long) query.getSingleResult();
//    }
}