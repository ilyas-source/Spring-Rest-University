package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Timeslot;

import java.util.List;
import java.util.Optional;

public interface TimeslotDao extends GeneralDao<Timeslot> {

    List<Timeslot> findAll();

    int countIntersectingTimeslots(Timeslot timeslot);

    Optional<Timeslot> findByBothTimes(Timeslot timeslot);

}