package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Timeslot;

import java.util.Optional;

public interface TimeslotDao extends GeneralDao<Timeslot> {

    int countIntersectingTimeslots(Timeslot timeslot);

    Optional<Timeslot> findByBothTimes(Timeslot timeslot);

}