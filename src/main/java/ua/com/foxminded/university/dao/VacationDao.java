package ua.com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import ua.com.foxminded.university.model.Vacation;

public interface VacationDao extends GeneralDao<Vacation> {

    List<Vacation> findByTeacherId(int id);

    int countIntersectingVacations(Vacation vacation);

    Optional<Vacation> findByBothDates(Vacation vacation);
}
