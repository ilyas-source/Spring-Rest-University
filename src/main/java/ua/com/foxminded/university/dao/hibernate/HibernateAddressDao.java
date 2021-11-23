package ua.com.foxminded.university.dao.hibernate;

public class HibernateAddressDao {

//    private static final Logger logger = LoggerFactory.getLogger(HibernateAddressDao.class);
//
//    private SessionFactory sessionFactory;
//
//    public HibernateAddressDao(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    @Override
//    public void create(Address address) {
//        Session session = sessionFactory.openSession();
//        session.save(address);
//    }
//
//    @Override
//    public Optional<Address> findById(int id) {
//        logger.debug("Getting by id: {}", id);
//        Session session = sessionFactory.openSession();
//        return Optional.ofNullable(session.get(Address.class, id));
//    }
//
//    @Override
//    public void update(Address address) {
//        logger.debug("Updating: {}", address);
//        Session session = sessionFactory.openSession();
//        session.merge(address);
//    }
//
//    @Override
//    public void delete(Address address) {
//        logger.debug("Deleting: {}", address);
//        Session session = sessionFactory.openSession();
//        session.delete(address);
//    }
//
//    @Override
//    public List<Address> findAll() {
//        logger.debug("Retrieving all addresses from DB");
//        Session session = sessionFactory.openSession();
//        return session.createNamedQuery("SelectAllAddresses").list();
//    }
}


