package ua.com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.university.model.Holiday;

public interface HolidayDao extends GeneralDao<Holiday> {

    List<Holiday> findByDate(LocalDate date);

}
