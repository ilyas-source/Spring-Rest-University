package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

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

    public void create(Address address) {
	addressDao.create(address);
    }

    public void update(Address address) {
	addressDao.update(address);
    }

    public List<Address> findAll() {
	return addressDao.findAll();
    }

    public Optional<Address> findById(int id) {
	return addressDao.findById(id);
    }

    public void delete(int id) {
	var canDelete = idExists(id) && addressIsFree(id);
	if (canDelete) {
	    addressDao.delete(id);
	} else {
	    System.out.println("Can't delete address");
	}

    }

    private boolean idExists(int id) {
	return addressDao.findById(id).isPresent();
    }

    private boolean addressIsFree(int id) {
	Optional<Teacher> teacher = teacherDao.findByAddressId(id);
	Optional<Student> student = studentDao.findByAddressId(id);
	return teacher.isEmpty() && student.isEmpty();
    }
}
