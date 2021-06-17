package ua.com.foxminded.university;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcAddressDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcHolidayDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcLectureDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcStudentDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcVacationDAO;
import ua.com.foxminded.university.menu.TeachersMenu;
import ua.com.foxminded.university.menu.VacationsMenu;
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
import ua.com.foxminded.university.model.Vacation;

@Component
public class JdbcUniversityPopulator {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcAddressDAO jdbcAddressDAO;
    @Autowired
    private JdbcSubjectDAO jdbcSubjectDAO;
    @Autowired
    private JdbcVacationDAO jdbcVacationDAO;
    @Autowired
    private JdbcClassroomDAO jdbcClassroomDAO;
    @Autowired
    private JdbcGroupDAO jdbcGroupDAO;
    @Autowired
    private JdbcLectureDAO jdbcLectureDAO;
    @Autowired
    private JdbcTeacherDAO jdbcTeacherDAO;
    @Autowired
    private JdbcHolidayDAO jdbcHolidayDAO;
    @Autowired
    private JdbcStudentDAO jdbcStudentDAO;

    @Autowired
    private TeachersMenu teachersMenu;
    @Autowired
    private VacationsMenu vacationsMenu;

    public void populate() {
	createEmptyDb(dataSource);
	populateAddresses();
	populateSubjects();
	populateVacations();
	populateAddresses();
	populateTeachers();
	populateStudents();
	populateClassRooms();
//	populateGroups();
//	populateLectures();
	populateHolidays();
    }

    private void createEmptyDb(DataSource dataSource) {
	Resource resource = new ClassPathResource("schema.sql");
	ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
	databasePopulator.execute(dataSource);
    }

    private void populateSubjects() {
	List<Subject> subjects = new ArrayList<>();
	subjects.add(new Subject("Economics", "Base economics"));
	subjects.add(new Subject("Philosophy", "Base philosophy"));
	subjects.add(new Subject("Chemistry", "Base chemistry"));
	subjects.add(new Subject("Radiology", "Explore radiation"));

	for (Subject s : subjects) {
	    jdbcSubjectDAO.addToDb(s);
	}
    }

    private void populateVacations() {
	List<Vacation> vacations = new ArrayList<>();
	vacations.add(new Vacation(LocalDate.of(2000, 01, 01), LocalDate.of(2000, 02, 01)));
	vacations.add(new Vacation(LocalDate.of(2000, 05, 01), LocalDate.of(2000, 06, 01)));
	vacations.add(new Vacation(LocalDate.of(2000, 01, 15), LocalDate.of(2000, 02, 15)));
	vacations.add(new Vacation(LocalDate.of(2000, 06, 01), LocalDate.of(2000, 07, 01)));

	for (Vacation v : vacations) {
	    jdbcVacationDAO.addToDb(v);
	}
    }

    private void populateAddresses() {
	List<Address> addresses = new ArrayList<>();
	addresses.add(new Address("UK", "12345", "City-Of-Edinburgh", "Edinburgh", "Panmure House"));
	addresses.add(new Address("Poland", "54321", "Central region", "Warsaw", "Urszuli Ledochowskiej 3"));
	addresses.add(new Address("Russia", "450080", "Permskiy kray", "Perm", "Lenina 5"));
	addresses.add(new Address("USA", "90210", "California", "LA", "Grove St. 15"));
	addresses.add(new Address("France", "21012", "Central", "Paris", "Rue 15"));
	addresses.add(new Address("China", "20121", "Guangdung", "Beijin", "Main St. 125"));

	for (Address a : addresses) {
	    jdbcAddressDAO.addToDb(a);
	}

    }

    private void populateTeachers() {
	List<Subject> subjects = jdbcSubjectDAO.findAll();
	List<Vacation> vacations = jdbcVacationDAO.findAll();

	List<Subject> subjects1 = new ArrayList<>();
	subjects1.add(subjects.get(0));
	subjects1.add(subjects.get(1));

	List<Vacation> vacations1 = new ArrayList<>();
	vacations1.add(vacations.get(0));
	vacations1.add(vacations.get(1));

	Teacher teacher = new Teacher("Adam", "Smith",
		Gender.MALE, Degree.DOCTOR,
		subjects1, "adam@smith.com", "+223322",
		jdbcAddressDAO.findById(1).orElse(null),
		vacations1);

	jdbcTeacherDAO.addToDb(teacher);

	List<Subject> subjects2 = new ArrayList<>();
	subjects2.add(subjects.get(2));
	subjects2.add(subjects.get(3));

	List<Vacation> vacations2 = new ArrayList<>();
	vacations2.add(vacations.get(2));
	vacations2.add(vacations.get(3));

	teacher = new Teacher("Marie", "Curie",
		Gender.FEMALE, Degree.MASTER,
		subjects2, "marie@curie.com", "+322223",
		jdbcAddressDAO.findById(2).orElse(null),
		vacations2);

	jdbcTeacherDAO.addToDb(teacher);
    }

    private void populateHolidays() {
	List<Holiday> holidays = new ArrayList<>();
	holidays.add(new Holiday(LocalDate.of(2000, 12, 25), "Christmas"));
	holidays.add(new Holiday(LocalDate.of(2000, 10, 30), "Halloween"));
	holidays.add(new Holiday(LocalDate.of(2000, 3, 8), "International women's day"));

	for (Holiday h : holidays) {
	    jdbcHolidayDAO.addToDb(h);
	}
    }

    private void populateStudents() {
	List<Student> students = new ArrayList<>();
	students.add(new Student("Ivan", "Petrov", Gender.MALE, LocalDate.of(1980, 11, 1),
		LocalDate.of(2000, 1, 1), "qwe@rty.com", "123123123",
		jdbcAddressDAO.findById(3).orElse(null)));

	students.add(new Student("John", "Doe", Gender.MALE, LocalDate.of(1981, 11, 1),
		LocalDate.of(2000, 1, 1), "qwe@qwe.com", "1231223",
		jdbcAddressDAO.findById(4).orElse(null)));

	students.add(new Student("Janna", "D'Ark", Gender.FEMALE, LocalDate.of(1881, 11, 1),
		LocalDate.of(2000, 1, 1), "qwe@no.fr", "1231223",
		jdbcAddressDAO.findById(5).orElse(null)));

	students.add(new Student("Mao", "Zedun", Gender.MALE, LocalDate.of(1921, 9, 14),
		LocalDate.of(2000, 1, 1), "qwe@no.cn", "1145223",
		jdbcAddressDAO.findById(6).orElse(null)));

	for (Student s : students) {
	    jdbcStudentDAO.addToDb(s);
	}
    }

    private void populateClassRooms() {

	List<Classroom> classrooms = new ArrayList<>();

	classrooms.add(new Classroom(new Location("Phys building", 2, 22), "Big physics auditory", 500));
	classrooms.add(new Classroom(new Location("Chem building", 1, 12), "Small chemistry auditory", 30));
	classrooms.add(new Classroom(new Location("Chem building", 2, 12), "Chemistry laboratory", 15));

	for (Classroom c : classrooms) {
	    jdbcClassroomDAO.addToDb(c);
	}
    }

    private void populateGroups() {
	List<Student> students = jdbcStudentDAO.findAll();
	List<Group> groups = new ArrayList<>();

	groups.add(new Group("AB-11", new ArrayList<>(List.of(students.get(1), students.get(2)))));
	groups.add(new Group("ZI-08", new ArrayList<>(List.of(students.get(3), students.get(4)))));

	for (Group g : groups) {
	    jdbcGroupDAO.addToDb(g);
	}
    }

    private void populateLectures() {
	List<Lecture> lectures = new ArrayList<>();
	List<Group> groups = jdbcGroupDAO.findAll();
	List<Subject> subjects = jdbcSubjectDAO.findAll();
	List<Teacher> teachers = jdbcTeacherDAO.findAll();
	List<Classroom> classRooms = jdbcClassroomDAO.findAll();

	lectures.add(new Lecture(LocalDate.of(2000, 1, 1),
		new TimeRange(LocalTime.of(9, 0), LocalTime.of(10, 0)),
		new ArrayList<>(List.of(groups.get(0))),
		subjects.get(0), teachers.get(0), classRooms.get(0)));

	lectures.add(new Lecture(LocalDate.of(2000, 1, 2),
		new TimeRange(LocalTime.of(10, 0), LocalTime.of(11, 0)),
		new ArrayList<>(List.of(groups.get(1))),
		subjects.get(1), teachers.get(1), classRooms.get(1)));

	for (Lecture l : lectures) {
	    jdbcLectureDAO.addToDb(l);
	}
    }
}
