package ua.com.foxminded.university.dao.hibernate;

public class HibernateLocationDao {
//
//    private static final Logger logger = LoggerFactory.getLogger(HibernateLocationDao.class);
//
//    private SessionFactory sessionFactory;
//
//    public HibernateLocationDao(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    @Override
//    public void create(Location location) {
//        Session session = sessionFactory.openSession();
//        session.save(location);
//    }
//
//    @Override
//    public Optional<Location> findById(int id) {
//        logger.debug("Getting by id: {}", id);
//        Session session = sessionFactory.openSession();
//        return Optional.ofNullable(session.get(Location.class, id));
//    }
//
//    @Override
//    public void update(Location location) {
//        logger.debug("Updating: {}", location);
//        Session session = sessionFactory.openSession();
//        session.merge(location);
//    }
//
//    @Override
//    public void delete(Location location) {
//        logger.debug("Deleting: {}", location);
//        Session session = sessionFactory.openSession();
//        session.delete(location);
//    }
//
//    @Override
//    public List<Location> findAll() {
//        logger.debug("Retrieving all locations from DB");
//        Session session = sessionFactory.openSession();
//        return session.createNamedQuery("SelectAllLocations").list();
//    }
}