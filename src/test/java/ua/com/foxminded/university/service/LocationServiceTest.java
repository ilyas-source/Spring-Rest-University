package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.dao.ClassroomDaoTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.dao.LocationDaoTest.TestData.expectedLocation1;
import static ua.com.foxminded.university.dao.LocationDaoTest.TestData.expectedLocations;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.LocationDao;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.exception.LocationInUseException;
import ua.com.foxminded.university.model.Location;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationDao locationDao;
    @Mock
    private ClassroomDao classroomDao;
    @InjectMocks
    private LocationService locationService;

    @Test
    void onFindAll_shouldReturnCorrectList() {
	when(locationDao.findAll()).thenReturn(expectedLocations);

	assertEquals(expectedLocations, locationService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectLocation() {
	when(locationDao.findById(1)).thenReturn(Optional.of(expectedLocation1));
	Optional<Location> expected = Optional.of(expectedLocation1);

	Optional<Location> actual = locationService.findById(1);

	assertEquals(expected, actual);
    }

    @Test
    void givenLocation_onCreate_shouldCallDaoCreate() {
	locationService.create(expectedLocation1);

	verify(locationDao).create(expectedLocation1);
    }

    @Test
    void givenLocation_onUpdate_shouldCallDaoUpdate() {
	locationService.update(expectedLocation1);

	verify(locationDao).update(expectedLocation1);
    }

    @Test
    void givenFreeLocation_onDelete_shouldCallDaoDelete() {
	when(classroomDao.findByLocation(expectedLocation1)).thenReturn(Optional.empty());
	when(locationDao.findById(expectedLocation1.getId())).thenReturn(Optional.of(expectedLocation1));

	locationService.delete(expectedLocation1.getId());

	verify(locationDao).delete(1);
    }

    @Test
    void givenUsedLocation_onDelete_shouldThrowException() {
	String expected = "Location is used for Big physics auditory";
	when(classroomDao.findByLocation(expectedLocation1)).thenReturn(Optional.of(expectedClassroom1));
	when(locationDao.findById(expectedLocation1.getId())).thenReturn(Optional.of(expectedLocation1));

	Throwable thrown = assertThrows(LocationInUseException.class,
		() -> locationService.delete(expectedLocation1.getId()));

	assertEquals(expected, thrown.getMessage());
	verify(locationDao, never()).delete(expectedLocation1.getId());
    }

    @Test
    void givenIncorrectLocationId_onDelete_shouldThrowException() {
	String expected = "Location with id=1 not found, nothing to delete";

	Throwable thrown = assertThrows(EntityNotFoundException.class,
		() -> locationService.delete(1));

	assertEquals(expected, thrown.getMessage());
	verify(locationDao, never()).delete(1);
    }
}