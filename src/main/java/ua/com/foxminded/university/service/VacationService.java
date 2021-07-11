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

    public void create(Vacation vacation) {
	vacationDao.create(vacation);
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
	boolean canDelete = idExists(id);
	if (canDelete) {
	    vacationDao.delete(id);
	} else {
	    System.out.println("Can't delete vacation");
	}
    }

    private boolean idExists(int id) {
	return vacationDao.findById(id).isPresent();
    }
}
