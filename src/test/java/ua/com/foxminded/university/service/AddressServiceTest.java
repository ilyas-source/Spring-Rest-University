package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.AddressDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private JdbcAddressDao addressDao;
    @InjectMocks
    private AddressService addressService;

    @Test
    void onFindAll_shouldReturnAllAddresss() {
	when(addressDao.findAll()).thenReturn(expectedAddresses);

	assertEquals(expectedAddresses, addressService.findAll());
    }

    @Test
    void givenCorrectId_onFindById_shouldReturnOptionalWithCorrectAddress() {
	when(addressDao.findById(1)).thenReturn(Optional.of(expectedAddress1));

	assertEquals(Optional.of(expectedAddress1), addressService.findById(1));
    }

    @Test
    void givenAddress_onCreate_shouldCallCreate() {
	addressService.create(expectedAddress1);

	verify(addressDao).create(expectedAddress1);
    }

    @Test
    void givenAddress_onUpdate_shouldCallUpdate() {
	addressService.update(expectedAddress1);

	verify(addressDao).update(expectedAddress1);
    }

    @Test
    void givenAddress_onDelete_shouldCallDelete() {
	addressService.delete(1);

	verify(addressDao).delete(1);
    }
}