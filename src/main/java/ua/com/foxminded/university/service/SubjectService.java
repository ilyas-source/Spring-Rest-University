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

    public void create(Subject subject) {
	// ��������� �� ������������ ��������
	subjectDao.create(subject);
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
	boolean canDelete = idExists(id);
	// ��������� ��� ������� �� �������� � ��������������
	// ��������� ��� �� �������� �� ������������� ������
	if (canDelete) {
	    subjectDao.delete(id);
	} else {
	    System.out.println("Can't delete subject");
	}
    }

    private boolean idExists(int id) {
	return subjectDao.findById(id).isPresent();
    }
}
