package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.AddressDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.*;
import static ua.com.foxminded.university.dao.StudentDaoTest.TestData.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private JdbcAddressDao addressDao;
    @Mock
    private TeacherDao teacherDao;
    @Mock
    private StudentDao studentDao;
    @InjectMocks
    private AddressService addressService;

    @Test
    void onFindAll_shouldCallDaoFindAll() {
	addressService.findAll();

	verify(addressDao).findAll();
    }

    @Test
    void givenId_onFindById_shouldCallDaoFindById() {
	addressService.findById(1);

	verify(addressDao).findById(1);
    }

    @Test
    void givenAddress_onCreate_shouldCallDaoCreate() throws Exception {
	addressService.create(expectedAddress1);

	verify(addressDao).create(expectedAddress1);
    }

    @Test
    void givenAddress_onUpdate_shouldCallUpdate() throws Exception {
	addressService.update(expectedAddress1);

	verify(addressDao).update(expectedAddress1);
    }

    @Test
    void givenTeachersAddressId_onDelete_shouldThrowException() throws Exception {
	when(teacherDao.findAll()).thenReturn(expectedTeachers);
	when(addressDao.findById(1)).thenReturn(Optional.of(expectedAddress1));
	String expected = "Address is used by a teacher account, can't delete address";

	Throwable thrown = assertThrows(Exception.class, () -> {
	    addressService.delete(1);
	});

	assertEquals(expected, thrown.getMessage());
    }

    @Test
    void givenStudentsAddressId_onDelete_shouldThrowException() throws Exception {
	when(studentDao.findAll()).thenReturn(expectedStudents);
	when(addressDao.findById(3)).thenReturn(Optional.of(expectedAddress3));
	String expected = "Address is used by a student account, can't delete address";

	Throwable thrown = assertThrows(Exception.class, () -> {
	    addressService.delete(3);
	});

	assertEquals(expected, thrown.getMessage());
    }

    @Test
    void givenWrongId_onDelete_shouldThrowException() throws Exception {
	when(addressDao.findById(10)).thenReturn(Optional.empty());
	String expected = String.format("Address not found", expectedAddress1.getId());

	Throwable thrown = assertThrows(Exception.class, () -> {
	    addressService.delete(10);
	});

	assertEquals(expected, thrown.getMessage());
    }

    @Test
    void givenCorrectUnusedId_onDelete_shouldCallDaoDelete() throws Exception {
	when(addressDao.findById(1)).thenReturn(Optional.of(expectedAddress1));

	addressService.delete(1);

	verify(addressDao).delete(1);
    }
}