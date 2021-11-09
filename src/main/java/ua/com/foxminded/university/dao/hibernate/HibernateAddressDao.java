package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.model.Address;

import java.util.List;
import java.util.Optional;

@Repository
public class HibernateAddressDao implements AddressDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateAddressDao.class);

    private SessionFactory sessionFactory;

    public HibernateAddressDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Address address) {
        Session session = sessionFactory.getCurrentSession();
        session.save(address);
    }

    @Override
    public Optional<Address> findById(int id) {
        logger.debug("Getting by id: {}", id);
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Address.class, id));
    }

    @Override
    public void update(Address address) {
        logger.debug("Updating: {}", address);
        Session session = sessionFactory.getCurrentSession();
        session.merge(address);
    }

    @Override
    public void delete(Address address) {
        logger.debug("Deleting: {}", address);
        Session session = sessionFactory.getCurrentSession();
        session.delete(address);
    }

    @Override
    public List<Address> findAll() {
        logger.debug("Retrieving all addresses from DB");
        Session session = sessionFactory.getCurrentSession();
        return session.createNamedQuery("SelectAllAddresses").list();
    }
}


