package ua.com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.exception.EntityInUseException;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.repository.ClassroomRepository;
import ua.com.foxminded.university.repository.LocationRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private LocationRepository locationRepository;
    private ClassroomRepository classroomRepository;

    public LocationService(LocationRepository locationRepository, ClassroomRepository classroomRepository) {
        this.locationRepository = locationRepository;
        this.classroomRepository = classroomRepository;
    }

    public void create(Location location) {
        logger.debug("Creating a new location: {} ", location);
        locationRepository.save(location);
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Optional<Location> findById(int id) {
        return locationRepository.findById(id);
    }

    public Location getById(int id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find location by id " + id));
    }

    public void update(Location location) {
        logger.debug("Updating location: {} ", location);
        locationRepository.save(location);
    }

    public void delete(int id) {
        logger.debug("Deleting location by id: {} ", id);
        Location location = getById(id);
        verifyIsNotUsed(location);
        locationRepository.delete(location);
    }

    private void verifyIsNotUsed(Location location) {
        Optional<Classroom> classroom = classroomRepository.findByLocation(location);
        if (classroom.isPresent()) {
            throw new EntityInUseException("Location is used for " + classroom.get().getName());
        }
    }
}
