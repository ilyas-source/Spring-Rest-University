package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Vacation;

import java.util.List;
import java.util.Optional;

public interface VacationDao extends GeneralDao<Vacation> {

    List<Vacation> findByTeacherId(int id);

    int countIntersectingVacations(Vacation vacation);

    Optional<Vacation> findByBothDates(Vacation vacation);
}
