package ua.com.foxminded.university.dao.hibernate;

public class HibernateGroupDao {
//
//    private static final Logger logger = LoggerFactory.getLogger(HibernateGroupDao.class);
//
//    private SessionFactory sessionFactory;
//
//    public HibernateGroupDao(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    @Override
//    public void create(Group group) {
//        Session session = sessionFactory.openSession();
//        session.save(group);
//    }
//
//    @Override
//    public Optional<Group> findById(int id) {
//        logger.debug("Getting by id: {}", id);
//        Session session = sessionFactory.openSession();
//        return Optional.ofNullable(session.get(Group.class, id));
//    }
//
//    @Override
//    public void update(Group group) {
//        logger.debug("Updating: {}", group);
//        Session session = sessionFactory.openSession();
//        session.merge(group);
//    }
//
//    @Override
//    public void delete(Group group) {
//        logger.debug("Deleting: {}", group);
//        Session session = sessionFactory.openSession();
//        session.delete(group);
//    }
//
//    @Override
//    public List<Group> findAll() {
//        logger.debug("Retrieving all groups from DB");
//        Session session = sessionFactory.openSession();
//        return session.createNamedQuery("SelectAllGroups").list();
//    }
//
//    @Override
//    public Optional<Group> findByName(String name) {
//        logger.debug("Searching group by name: {}", name);
//        Session session = sessionFactory.openSession();
//        Query<Group> query = session.createNamedQuery("FindGroupByName")
//                .setParameter("name", name);
//        try {
//            return Optional.of(query.getSingleResult());
//        } catch (NoResultException e) {
//            return Optional.empty();
//        }
//    }

//    @Override
//    public List<Group> findByLectureId(int lectureId) {
//        logger.debug("Searching by lecture id: {}", lectureId);
//        Session session = sessionFactory.openSession();
//        String sqlString = "SELECT g.id, g.name from lectures_groups AS l_g LEFT JOIN groups AS g " +
//                "ON (l_g.group_id=g.id) WHERE l_g.lecture_id = :lectureId";
//
//        return session.createSQLQuery(sqlString)
//                .addEntity(Group.class)
//                .setParameter("lectureId", lectureId)
//                .list();
//    }
}
