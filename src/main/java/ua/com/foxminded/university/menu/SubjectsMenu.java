package ua.com.foxminded.university.menu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

import static ua.com.foxminded.university.Menu.*;

import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import ua.com.foxminded.university.model.Subject;

@Component
public class SubjectsMenu {

    JdbcSubjectDao jdbcSubjectDao;

    public SubjectsMenu(JdbcSubjectDao jdbcSubjectDao) {
	this.jdbcSubjectDao = jdbcSubjectDao;
    }

    public String getStringOfSubjects(List<Subject> subjects) {
	StringBuilder result = new StringBuilder();
	subjects.sort(Comparator.comparing(Subject::getId));

	for (Subject subject : subjects) {
	    result.append(getStringFromSubject(subject) + CR);
	}
	return result.toString();
    }

    public String getStringFromSubject(Subject subject) {
	return subject.getId() + ". " + subject.getName() + ": " + subject.getDescription();
    }

    public void addSubject() {
	jdbcSubjectDao.create(createSubject());
    }

    public Subject createSubject() {
	System.out.print("Enter subject name: ");
	String name = scanner.nextLine();
	System.out.print("Enter description: ");
	String description = scanner.nextLine();

	return new Subject(name, description);
    }

    public void printSubjects() {
	System.out.println(getStringOfSubjects(jdbcSubjectDao.findAll()));
    }

    public void updateSubject() {
	Subject oldSubject = selectSubject();
	Subject newSubject = createSubject();
	newSubject.setId(oldSubject.getId());
	jdbcSubjectDao.update(newSubject);
	System.out.println("Overwrite successful.");
    }

    public void deleteSubject() {
	jdbcSubjectDao.delete(selectSubject().getId());
	System.out.println("Subject deleted successfully.");
    }

    public Subject selectSubject() {
	List<Subject> subjects = jdbcSubjectDao.findAll();
	Subject result = null;

	while (isNull(result)) {
	    System.out.println("Select subject: ");
	    System.out.print(getStringOfSubjects(subjects));
	    int choice = getIntFromScanner();
	    Optional<Subject> selectedSubject = jdbcSubjectDao.findById(choice);
	    if (selectedSubject.isEmpty()) {
		System.out.println("No such subject.");
	    } else {
		result = selectedSubject.get();
		System.out.println("Success.");
	    }
	}
	return result;
    }

    public List<Subject> selectSubjects() {
	List<Subject> result = new ArrayList<>();
	List<Subject> subjects = jdbcSubjectDao.findAll();
	boolean finished = false;
	boolean correctEntry = false;

	System.out.println("Selecting subjects.");

	while (!(finished && correctEntry)) {
	    if (!result.isEmpty()) {
		System.out.println("Assigned subjects:");
		System.out.print(getStringOfSubjects(result));
	    }
	    System.out.println("Enter a new subject number to add to this teacher: ");
	    System.out.print(getStringOfSubjects(subjects));
	    correctEntry = false;
	    int choice = getIntFromScanner();
	    if (choice <= subjects.size()) {
		Subject selected = subjects.get(choice - 1);
		if (result.contains(selected)) {
		    System.out.println("Subject already assigned to the teacher.");
		} else {
		    correctEntry = true;
		    result.add(new Subject(selected.getId(), selected.getName(), selected.getDescription()));
		    System.out.println("Success.");
		}
	    } else {
		System.out.println("No such subject.");
	    }
	    System.out.print("Add another subject? (y/n): ");
	    String entry = scanner.nextLine().toLowerCase();
	    if (!entry.equals("y")) {
		finished = true;
	    }
	}
	return result;
    }
}
