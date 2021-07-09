package ua.com.foxminded.university.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Address;

@Service
public class AddressService {

    private AddressDao addressDao;
    private TeacherDao teacherDao;
    private StudentDao studentDao;

    public AddressService(AddressDao jdbcAddressDao, TeacherDao teacherDao, StudentDao studentDao) {
	this.addressDao = jdbcAddressDao;
	this.teacherDao = teacherDao;
	this.studentDao = studentDao;
    }

    public void create(Address address) throws Exception {
	addressDao.create(address);
    }

    public void update(Address address) throws Exception {
	addressDao.update(address);
    }

    public List<Address> findAll() {
	return addressDao.findAll();
    }

    public Optional<Address> findById(int id) {
	return addressDao.findById(id);
    }

    public void delete(int id) throws Exception {
	verifyAddressExists(id);
	verifyAddressIsFree(id);
	addressDao.delete(id);
    }

    private void verifyAddressExists(int id) throws Exception {
	if (!addressDao.findById(id).isPresent()) {
	    throw new Exception("Address not found");
	}
    }

    private void verifyAddressIsFree(int id) throws Exception {
	var address = addressDao.findById(id).get();
	List<Address> teacherAddresses = teacherDao.findAll()
		.stream()
		.flatMap(p -> Stream.of(p.getAddress()))
		.collect(toList());
	if (teacherAddresses.contains(address)) {
	    throw new Exception("Address is used by a teacher account, can't delete address");
	}
	List<Address> studentAddresses = studentDao.findAll()
		.stream()
		.flatMap(p -> Stream.of(p.getAddress()))
		.collect(toList());
	if (studentAddresses.contains(address)) {
	    throw new Exception("Address is used by a student account, can't delete address");
	}
    }

}
