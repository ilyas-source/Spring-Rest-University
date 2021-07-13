package ua.com.foxminded.university.service;

import java.time.Duration;
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
	if (hasNoIntersections(vacation)) {
	    vacationDao.create(vacation);
	}
    }

    private boolean hasNoIntersections(Vacation vacation) {
	return vacationDao.findAll()
		.stream()
		.flatMap(v -> Stream.of(v.intersects(vacation)))
		.filter(b -> b == true)
		.findFirst()
		.isEmpty();
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
	}
    }

    private boolean idExists(int id) {
	return vacationDao.findById(id).isPresent();
    }

    public int countLength(Vacation vacation) {
	return (int) (Duration.between(vacation.getStartDate(), vacation.getEndDate()).toDays() + 1);
    }
}
