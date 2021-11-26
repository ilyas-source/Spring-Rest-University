package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.university.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

}
