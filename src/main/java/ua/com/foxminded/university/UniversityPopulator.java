package ua.com.foxminded.university;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.University;
import ua.com.foxminded.university.model.Vacation;

public class UniversityPopulator {

    University university;

    public UniversityPopulator(University university) {
	this.university = university;
    }

    public void populate() {
	populateSubjects();
	populateTeachers();
    }

    private void populateSubjects() {
	List<Subject> subjects = new ArrayList<>();
	subjects.add(new Subject("Economics", "Base economics"));
	subjects.add(new Subject("Phylosophy", "Base phylosophy"));
	subjects.add(new Subject("Chemistry", "Base chemistry"));
	subjects.add(new Subject("Radiology", "Explore radiation"));

	university.setSubjects(subjects);
    }

    private void populateTeachers() {
	List<Teacher> teachers = new ArrayList<>();

	List<Subject> subjects = new ArrayList<>();
	subjects.add(new Subject("Economics", "Base economics"));
	subjects.add(new Subject("Phylosophy", "Base phylosophy"));

	List<Vacation> vacations = new ArrayList<>();
	vacations.add(new Vacation(LocalDate.of(2000, 01, 01), LocalDate.of(2000, 02, 01)));
	vacations.add(new Vacation(LocalDate.of(2000, 05, 01), LocalDate.of(2000, 06, 01)));

	Teacher teacher = new Teacher("Adam", "Smith",
		Gender.MALE, Degree.DOCTOR,
		subjects, "adam@smith.com", "+223322",
		new Address("UK", "12345", "City-Of-Edinburgh", "Edinburgh", "Panmure House"),
		vacations);

	teachers.add(teacher);

	List<Subject> subjects2 = new ArrayList<>();
	subjects2.add(new Subject("Chemistry", "Base chemistry"));
	subjects2.add(new Subject("Radiology", "Explore radiation"));

	List<Vacation> vacations2 = new ArrayList<>();
	vacations2.add(new Vacation(LocalDate.of(2000, 01, 15), LocalDate.of(2000, 02, 15)));
	vacations2.add(new Vacation(LocalDate.of(2000, 06, 01), LocalDate.of(2000, 07, 01)));

	teacher = new Teacher("Marie", "Curie",
		Gender.FEMALE, Degree.MASTER,
		subjects2, "marie@curie.com", "+322223",
		new Address("Poland", "54321", "Central region", "Warsaw", "Urszuli Ledochowskiej 3"),
		vacations2);

	teachers.add(teacher);

	university.setTeachers(teachers);
    }
}
