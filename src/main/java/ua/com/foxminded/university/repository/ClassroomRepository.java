package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

    Optional<Classroom> findByName(String name);

    Optional<Classroom> findByLocation(Location location);
}
