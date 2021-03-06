package ua.com.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.exception.EntityInUseException;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.repository.ClassroomRepository;
import ua.com.foxminded.university.repository.LocationRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.foxminded.university.service.ClassroomServiceTest.TestData.expectedClassroom1;
import static ua.com.foxminded.university.service.LocationServiceTest.TestData.expectedLocation1;
import static ua.com.foxminded.university.service.LocationServiceTest.TestData.expectedLocations;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;
    @Mock
    private ClassroomRepository classroomRepository;
    @InjectMocks
    private LocationService locationService;

    @Test
    void onFindAll_shouldReturnCorrectList() {
        when(locationRepository.findAll()).thenReturn(expectedLocations);

        assertEquals(expectedLocations, locationService.findAll());
    }

    @Test
    void givenId_onFindById_shouldReturnOptionalWithCorrectLocation() {
        when(locationRepository.findById(1)).thenReturn(Optional.of(expectedLocation1));
        Optional<Location> expected = Optional.of(expectedLocation1);

        Optional<Location> actual = locationService.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenLocation_onCreate_shouldCallRepositoryCreate() {
        locationService.create(expectedLocation1);

        verify(locationRepository).save(expectedLocation1);
    }

    @Test
    void givenLocation_onUpdate_shouldCallRepositoryUpdate() {
        locationService.update(expectedLocation1);

        verify(locationRepository).save(expectedLocation1);
    }

    @Test
    void givenFreeLocation_onDelete_shouldCallRepositoryDelete() {
        when(classroomRepository.findByLocation(expectedLocation1)).thenReturn(Optional.empty());
        when(locationRepository.findById(expectedLocation1.getId())).thenReturn(Optional.of(expectedLocation1));

        locationService.delete(expectedLocation1.getId());

        verify(locationRepository).delete(expectedLocation1);
    }

    @Test
    void givenUsedLocation_onDelete_shouldThrowException() {
        String expected = "Location is used for Big physics auditory";
        when(classroomRepository.findByLocation(expectedLocation1)).thenReturn(Optional.of(expectedClassroom1));
        when(locationRepository.findById(expectedLocation1.getId())).thenReturn(Optional.of(expectedLocation1));

        Throwable thrown = assertThrows(EntityInUseException.class,
                () -> locationService.delete(expectedLocation1.getId()));

        assertEquals(expected, thrown.getMessage());
        verify(locationRepository, never()).delete(expectedLocation1);
    }

    @Test
    void givenIncorrectLocationId_onDelete_shouldThrowException() {
        String expected = "Can't find location by id 1";

        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> locationService.delete(1));

        assertEquals(expected, thrown.getMessage());
        verify(locationRepository, never()).delete(any());
    }

    public interface TestData {
        Location expectedLocation1 = new Location(1, "Phys building", 2, 22);
        Location expectedLocation2 = new Location(2, "Chem building", 1, 12);
        Location expectedLocation3 = new Location(3, "Chem building", 2, 12);

        List<Location> expectedLocations = new ArrayList<>(
                Arrays.asList(expectedLocation1, expectedLocation2, expectedLocation3));
    }
}