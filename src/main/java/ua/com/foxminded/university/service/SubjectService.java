package ua.com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.SubjectDao;
import ua.com.foxminded.university.model.Subject;

@Service
public class SubjectService {

    private SubjectDao subjectDao;

    public SubjectService(SubjectDao subjectDao) {
	this.subjectDao = subjectDao;
    }

    public void create(Subject createSubject) {
	// ��������� �� ������������ ��������
	subjectDao.create(createSubject);
    }

    public List<Subject> findAll() {
	return subjectDao.findAll();
    }

    public Optional<Subject> findById(int choice) {
	return subjectDao.findById(choice);
    }

    public void update(Subject newSubject) {
	// ��������� �� ������������ ��������
	subjectDao.update(newSubject);
    }

    public void delete(int id) {
	// ��������� ��� ������� �� �������� � ��������������
	// ��������� ��� �� �������� �� ������������� ������
	subjectDao.delete(id);
    }
}
