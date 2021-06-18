package ua.com.foxminded.university.menu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

import static ua.com.foxminded.university.Menu.*;

import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDAO;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;

@Component
public class SubjectsMenu {

    @Autowired
    JdbcSubjectDAO jdbcSubjectDAO;

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
	jdbcSubjectDAO.addToDb(createSubject());
    }

    public Subject createSubject() {
	System.out.print("Enter subject name: ");
	String name = scanner.nextLine();
	System.out.print("Enter description: ");
	String description = scanner.nextLine();

	return new Subject(name, description);
    }

    public void printSubjects() {
	System.out.println(getStringOfSubjects(jdbcSubjectDAO.findAll()));
    }

    public void updateSubject() {
	List<Subject> subjects = jdbcSubjectDAO.findAll();

	System.out.println("Select a subject to update: ");
	System.out.println(getStringOfSubjects(subjects));
	int choice = getIntFromScanner();
	Subject subject = jdbcSubjectDAO.findById(choice).orElse(null);

	if (isNull(subject)) {
	    System.out.println("No such subject, returning...");
	} else {
	    subject = createSubject();
	    subject.setId(choice);
	    jdbcSubjectDAO.update(subject);

	    System.out.println("Overwrite successful.");
	}
    }

    public void deleteSubject() {
	List<Subject> subjects = jdbcSubjectDAO.findAll();

	System.out.println("Select a subject to delete: ");
	System.out.println(getStringOfSubjects(subjects));
	int choice = getIntFromScanner();
	Subject subject = jdbcSubjectDAO.findById(choice).orElse(null);

	if (isNull(subject)) {
	    System.out.println("No such subject, returning...");
	} else {
	    jdbcSubjectDAO.delete(choice);
	    System.out.println("Subject deleted successfully.");
	}
    }

    public Subject selectSubject() {
	List<Subject> subjects = jdbcSubjectDAO.findAll();
	Subject result = null;

	while (isNull(result)) {
	    System.out.println("Select subject: ");
	    System.out.print(getStringOfSubjects(subjects));
	    int choice = getIntFromScanner();
	    if (choice > subjects.size()) {
		System.out.println("No such object.");
	    } else {
		result = subjects.get(choice - 1);
		System.out.println("Success.");
	    }
	}
	return result;
    }

    public List<Subject> selectSubjects() {
	List<Subject> result = new ArrayList<>();
	List<Subject> subjects = jdbcSubjectDAO.findAll();
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
		    result.add(new Subject(selected.getName(), selected.getDescription()));
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
