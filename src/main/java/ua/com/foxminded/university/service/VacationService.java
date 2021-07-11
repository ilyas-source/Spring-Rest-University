package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.Vacation;

@Service
public class VacationService {

    private VacationDao vacationDao;

    public VacationService(VacationDao vacationDao) {
	this.vacationDao = vacationDao;
    }

    public void create(Vacation createVacation) {
	vacationDao.create(createVacation);
    }

    public List<Vacation> findAll() {
	return vacationDao.findAll();
    }

    public Optional<Vacation> findById(int choice) {
	return vacationDao.findById(choice);
    }

    public void update(Vacation newVacation) {
	vacationDao.update(newVacation);
    }

    public void delete(int id) {
	vacationDao.delete(id);
    }
}
