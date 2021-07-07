package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.LocationDaoTest.TestData.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.jdbc.JdbcLocationDao;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private JdbcLocationDao locationDao;
    @InjectMocks
    private LocationService locationService;

    @Test
    void onFindAll_shouldReturnAllLocations() {
	when(locationDao.findAll()).thenReturn(expectedLocations);

	assertEquals(expectedLocations, locationService.findAll());
    }

    @Test
    void givenCorrectId_onFindById_shouldReturnOptionalWithCorrectLocation() {
	when(locationDao.findById(1)).thenReturn(Optional.of(expectedLocation1));

	assertEquals(Optional.of(expectedLocation1), locationService.findById(1));
    }

    @Test
    void givenLocation_onCreate_shouldCallCreate() {
	locationService.create(expectedLocation1);

	verify(locationDao).create(expectedLocation1);
    }

    @Test
    void givenLocation_onUpdate_shouldCallUpdate() {
	locationService.update(expectedLocation1);

	verify(locationDao).update(expectedLocation1);
    }

    @Test
    void givenLocation_onDelete_shouldCallDelete() {
	locationService.delete(1);

	verify(locationDao).delete(1);
    }
}