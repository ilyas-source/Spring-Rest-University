package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Holiday;

import java.time.LocalDate;
import java.util.List;

public interface HolidayDao extends GeneralDao<Holiday> {

    List<Holiday> findAll();

    List<Holiday> findByDate(LocalDate date);

}
