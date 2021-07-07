package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.jdbc.JdbcVacationDao;
import ua.com.foxminded.university.model.Vacation;

@Service
public class VacationService {

    private JdbcVacationDao jdbcVacationDao;

    public VacationService(JdbcVacationDao jdbcVacationDao) {
	this.jdbcVacationDao = jdbcVacationDao;
    }

    public void create(Vacation createVacation) {
	jdbcVacationDao.create(createVacation);
    }

    public List<Vacation> findAll() {
	return jdbcVacationDao.findAll();
    }

    public Optional<Vacation> findById(int choice) {
	return jdbcVacationDao.findById(choice);
    }

    public void update(Vacation newVacation) {
	jdbcVacationDao.update(newVacation);
    }

    public void delete(int id) {
	jdbcVacationDao.delete(id);
    }
}
