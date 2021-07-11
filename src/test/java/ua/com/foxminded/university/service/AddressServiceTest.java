package ua.com.foxminded.university.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.AddressDaoTest.TestData.expectedAddress1;
import static ua.com.foxminded.university.dao.StudentDaoTest.TestData.expectedStudent1;
import static ua.com.foxminded.university.dao.TeacherDaoTest.TestData.expectedTeacher1;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.TeacherDao;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressDao addressDao;
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
    void givenAddress_onCreate_shouldCallDaoCreate() {
	addressService.create(expectedAddress1);

	verify(addressDao).create(expectedAddress1);
    }

    @Test
    void givenAddress_onUpdate_shouldCallUpdate() {
	addressService.update(expectedAddress1);

	verify(addressDao).update(expectedAddress1);
    }

    @Test
    void givenTeachersAddressId_onDelete_shouldNotCallDaoDelete() {
	when(addressDao.findById(1)).thenReturn(Optional.of(expectedAddress1));
	when(teacherDao.findByAddressId(1)).thenReturn(Optional.of(expectedTeacher1));

	addressService.delete(expectedAddress1.getId());

	verify(addressDao, never()).delete(expectedAddress1.getId());
    }

    @Test
    void givenStudentsAddressId_onDelete_shouldNotCallDaoDelete() {
	when(addressDao.findById(1)).thenReturn(Optional.of(expectedAddress1));
	when(studentDao.findByAddressId(1)).thenReturn(Optional.of(expectedStudent1));

	addressService.delete(expectedAddress1.getId());

	verify(addressDao, never()).delete(expectedAddress1.getId());
    }

    @Test
    void givenWrongId_onDelete_shouldNotCallDaoDelete() {
	when(addressDao.findById(10)).thenReturn(Optional.empty());

	addressService.delete(10);

	verify(addressDao, never()).delete(10);
    }

    @Test
    void givenCorrectUnusedAddressId_onDelete_shouldCallDaoDelete() {
	when(addressDao.findById(1)).thenReturn(Optional.of(expectedAddress1));

	addressService.delete(1);

	verify(addressDao).delete(1);
    }
}