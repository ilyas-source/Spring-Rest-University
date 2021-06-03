package ua.com.foxminded.university;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcHolidayDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcLectureDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcStudentDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDAO;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.TimeRange;
import ua.com.foxminded.university.model.University;
import ua.com.foxminded.university.model.Vacation;

@Component
public class JdbcUniversityPopulator {

    private University university;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcSubjectDAO jdbcSubjectDAO;

    public JdbcUniversityPopulator(University university) {
	this.university = university;
    }

    public void populate() {
	createEmptyDb(dataSource);
	populateSubjectsJDBC();
	populateTeachersJDBC();
	populateStudentsJDBC();
	populateClassRoomsJDBC();
	populateGroupsJDBC();
//	populateLecturesJDBC();
	populateHolidaysJDBC();
    }

    private void createEmptyDb(DataSource dataSource) {
	Resource resource = new ClassPathResource("schema.sql");
	ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
	databasePopulator.execute(dataSource);
    }

    private void populateSubjectsJDBC() {
	List<Subject> subjects = new ArrayList<>();
	subjects.add(new Subject("Economics", "Base economics"));
	subjects.add(new Subject("Philosophy", "Base philosophy"));
	subjects.add(new Subject("Chemistry", "Base chemistry"));
	subjects.add(new Subject("Radiology", "Explore radiation"));

	for (Subject s : subjects) {
	    jdbcSubjectDAO.create(s);
	}
    }

    private void populateTeachersJDBC() {
	List<Teacher> teachers = new ArrayList<>();
	List<Subject> subjects = jdbcSubjectDAO.findAll();

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
//	todo:
//	for(Teacher t: teachers) {
//	    jdbcTeacherDAO.create(t);
//	}
    }

    private void populateHolidaysJDBC() {
	List<Holiday> holidays = new ArrayList<>();
	holidays.add(new Holiday(LocalDate.of(2000, 12, 25), "Christmas"));
	holidays.add(new Holiday(LocalDate.of(2000, 10, 30), "Halloween"));
	holidays.add(new Holiday(LocalDate.of(2000, 3, 8), "International women's day"));

	university.setHolidays(holidays);
//	todo:
//	for(Holiday h: holidays) {
//	    JdbcHolidayDAO.create(h);
//	}
    }

    private void populateStudentsJDBC() {
	List<Student> students = new ArrayList<>();
	students.add(new Student("Ivan", "Petrov", Gender.MALE, LocalDate.of(1980, 11, 1),
		LocalDate.of(2000, 1, 1), "qwe@rty.com", "123123123",
		new Address("Russia", "450080", "Permskiy kray", "Perm", "Lenina 5")));

	students.add(new Student("John", "Doe", Gender.MALE, LocalDate.of(1981, 11, 1),
		LocalDate.of(2000, 1, 1), "qwe@qwe.com", "1231223",
		new Address("USA", "90210", "California", "LA", "Grove St. 15")));

	students.add(new Student("Janna", "D'Ark", Gender.FEMALE, LocalDate.of(1881, 11, 1),
		LocalDate.of(2000, 1, 1), "qwe@no.fr", "1231223",
		new Address("France", "21012", "Central", "Paris", "Rue 15")));

	students.add(new Student("Mao", "Zedun", Gender.MALE, LocalDate.of(1921, 9, 14),
		LocalDate.of(2000, 1, 1), "qwe@no.cn", "1145223",
		new Address("China", "20121", "Guangdung", "Beijin", "Main St. 125")));

	university.setStudents(students);
//	todo:
//	for(Student s: students) {
//	    jdbcStudentDAO.create(s);
//	}

    }

    private void populateClassRoomsJDBC() {
	List<Classroom> classrooms = new ArrayList<>();

	classrooms.add(new Classroom(new Location("Phys building", 2, 22), "Big physics auditory", 500));
	classrooms.add(new Classroom(new Location("Chem building", 1, 12), "Small chemistry auditory", 30));
	classrooms.add(new Classroom(new Location("Chem building", 2, 12), "Chemistry laboratory", 15));

	university.setClassrooms(classrooms);
//	todo:
//	for(Classroom c: classrooms) {
//	    JdbcClassroomDAO.create(c);
//	}
    }

    private void populateGroupsJDBC() {
	List<Group> groups = new ArrayList<>();
	List<Student> students = university.getStudents();

	groups.add(new Group("AB-11", new ArrayList<>(List.of(students.get(0), students.get(1)))));
	groups.add(new Group("ZI-08", new ArrayList<>(List.of(students.get(2), students.get(3)))));

	university.setGroups(groups);
//	todo:
//	for(Group g: groups) {
//	    JdbcGroupDAO.create(g);
//	}
    }

//    private void populateLecturesJDBC() {
//	List<Lecture> lectures = new ArrayList<>();
//	List<Group> groups = university.getGroups();
//	List<Subject> subjects = university.getSubjects();
//	List<Teacher> teachers = university.getTeachers();
//	List<Classroom> classRooms = university.getClassrooms();
//
//	lectures.add(new Lecture(LocalDate.of(2000, 1, 1),
//		new TimeRange(LocalTime.of(9, 0), LocalTime.of(10, 0)),
//		new ArrayList<>(List.of(groups.get(0))),
//		subjects.get(0), teachers.get(0), classRooms.get(0)));
//
//	lectures.add(new Lecture(LocalDate.of(2000, 1, 2),
//		new TimeRange(LocalTime.of(10, 0), LocalTime.of(11, 0)),
//		new ArrayList<>(List.of(groups.get(1))),
//		subjects.get(1), teachers.get(1), classRooms.get(1)));
//
//	university.setLectures(lectures);
//	todo:
//	for(Lecture l: lectures) {
//	    jdbcLectureDAO.create(l);
//	}
//    }

}
