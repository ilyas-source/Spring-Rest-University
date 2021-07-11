package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
	if (noIntersections(vacation)) {
	    vacationDao.create(vacation);
	} else {
	    System.out.println("Can't create vacation");
	}
    }

    private boolean noIntersections(Vacation vacation) {
	boolean result = vacationDao.findAll()
		.stream()
		.flatMap(v -> Stream.of(v.intersects(vacation)))
		.filter(b -> b == true)
		.findFirst()
		.isEmpty();
	System.out.println("No intersections with existing vacations: " + result);
	return result;
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
