package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.university.model.Timeslot;

import java.time.LocalTime;
import java.util.Optional;

public interface TimeslotRepository extends JpaRepository<Timeslot, Integer> {

   Optional<Timeslot> findByBeginTimeAndEndTime(LocalTime beginTime, LocalTime endTime);

    long countByEndTimeIsGreaterThanEqualAndBeginTimeIsLessThanEqual(LocalTime endTime, LocalTime beginTime);
}
