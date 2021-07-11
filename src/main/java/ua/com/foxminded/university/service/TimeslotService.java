package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.TimeslotDao;
import ua.com.foxminded.university.model.Timeslot;

@Service
public class TimeslotService {

    private TimeslotDao timeslotDao;

    public TimeslotService(TimeslotDao timeslotDao) {
	this.timeslotDao = timeslotDao;
    }

    public void create(Timeslot createTimeslot) {
	// ���������, ��� �������� �� ���������� ������������
	// ��������� ��� �� ������ ���������� ������� 15 �����
	// ���������, ��� �� �� ������ 15 �����
	timeslotDao.create(createTimeslot);
    }

    public List<Timeslot> findAll() {
	return timeslotDao.findAll();
    }

    public Optional<Timeslot> findById(int choice) {
	return timeslotDao.findById(choice);
    }

    public void update(Timeslot newTimeslot) {
	// ���������, ��� �������� �� ���������� ������������
	// ��������� ��� �� ������ ���������� ������� 15 �����
	// ���������, ��� �� �� ������ 15 �����
	timeslotDao.update(newTimeslot);
    }

    public void delete(int id) {
	// ���������, ��� � ���� �������� �� ������������� ������
	timeslotDao.delete(id);
    }
}
