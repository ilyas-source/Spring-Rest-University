package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;
import ua.com.foxminded.university.model.Address;

@Service
public class AddressService {

    private JdbcAddressDao jdbcAddressDao;

    public AddressService(JdbcAddressDao jdbcAddressDao) {
	this.jdbcAddressDao = jdbcAddressDao;
    }

    public void create(Address createAddress) {
	jdbcAddressDao.create(createAddress);
    }

    public List<Address> findAll() {
	return jdbcAddressDao.findAll();
    }

    public Optional<Address> findById(int choice) {
	return jdbcAddressDao.findById(choice);
    }

    public void update(Address newAddress) {
	jdbcAddressDao.update(newAddress);
    }

    public void delete(int id) {
	jdbcAddressDao.delete(id);
    }
}
