package ua.com.foxminded.university.dao.jdbc;

import static java.util.function.Predicate.not;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.jdbc.mappers.TeacherMapper;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

@Component
public class JdbcTeacherDao implements TeacherDao {

    private static final String CREATE = "INSERT INTO teachers (first_name, last_name, gender, degree, "
	    + "email, phone, address_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE teachers SET first_name = ?, last_name = ?, gender = ?, " +
	    " degree = ?, email = ?, phone = ?, address_id = ? WHERE id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM teachers WHERE id = ?";
    private static final String FIND_BY_ADDRESS_ID = "SELECT * FROM teachers WHERE address_id = ?";
    private static final String FIND_ALL = "SELECT * FROM teachers";
    private static final String DELETE_BY_ID = "DELETE FROM teachers WHERE id = ?";

    private static final String REMOVE_SUBJECT = "DELETE FROM teachers_subjects where teacher_id = ? AND subject_id = ?";
    private static final String ASSIGN_SUBJECT = "INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (?, ?)";

    private static final String REMOVE_VACATION = "DELETE FROM vacations where teacher_id = ? AND start_date = ? AND end_date  = ?";
    private static final String ASSIGN_VACATION = "INSERT INTO vacations (teacher_id, start_date, end_date) VALUES (?, ?, ?)";

    private JdbcTemplate jdbcTemplate;
    private TeacherMapper teacherMapper;
    private AddressDao addressDao;

    public JdbcTeacherDao(JdbcTemplate jdbcTemplate, TeacherMapper teacherMapper, AddressDao jdbcAddressDao) {
	this.jdbcTemplate = jdbcTemplate;
	this.teacherMapper = teacherMapper;
	this.addressDao = jdbcAddressDao;
    }

    @Override
    @Transactional
    public void create(Teacher teacher) {
	KeyHolder keyHolder = new GeneratedKeyHolder();

	addressDao.create(teacher.getAddress());

	jdbcTemplate.update(connection -> {
	    PreparedStatement ps = connection
		    .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
	    ps.setString(1, teacher.getFirstName());
	    ps.setString(2, teacher.getLastName());
	    ps.setString(3, teacher.getGender().toString());
	    ps.setString(4, teacher.getDegree().toString());
	    ps.setString(5, teacher.getEmail());
	    ps.setString(6, teacher.getPhoneNumber());
	    ps.setInt(7, teacher.getAddress().getId());
	    return ps;
	}, keyHolder);

	teacher.setId((int) keyHolder.getKeys().get("id"));

	for (Subject subject : teacher.getSubjects()) {
	    assignSubject(subject, teacher);
	}

	for (Vacation vacation : teacher.getVacations()) {
	    assignVacation(vacation, teacher);
	}
    }

    @Override
    @Transactional
    public void update(Teacher teacher) {
	addressDao.update(teacher.getAddress());

	jdbcTemplate.update(UPDATE, teacher.getFirstName(), teacher.getLastName(),
		teacher.getGender().toString(), teacher.getDegree().toString(),
		teacher.getEmail(), teacher.getPhoneNumber(), teacher.getAddress().getId(),
		teacher.getId());

	List<Subject> newSubjects = teacher.getSubjects();
	List<Subject> oldSubjects = findById(teacher.getId()).get().getSubjects();

	List<Vacation> newVacations = teacher.getVacations();
	List<Vacation> oldVacations = findById(teacher.getId()).get().getVacations();

	oldSubjects.stream()
		.filter(not(newSubjects::contains))
		.forEach(s -> removeSubject(s, teacher));

	newSubjects.stream()
		.filter(not(oldSubjects::contains))
		.forEach(s -> assignSubject(s, teacher));

	oldVacations.stream()
		.filter(not(newVacations::contains))
		.forEach(v -> removeVacation(v, teacher));

	newVacations.stream()
		.filter(not(oldVacations::contains))
		.forEach(v -> assignVacation(v, teacher));
    }

    private void removeSubject(Subject subject, Teacher teacher) {
	jdbcTemplate.update(REMOVE_SUBJECT, teacher.getId(), subject.getId());
    }

    private void assignSubject(Subject subject, Teacher teacher) {
	jdbcTemplate.update(ASSIGN_SUBJECT, teacher.getId(), subject.getId());
    }

    private void removeVacation(Vacation vacation, Teacher teacher) {
	jdbcTemplate.update(REMOVE_VACATION, teacher.getId(), vacation.getStartDate(), vacation.getEndDate());
    }

    private void assignVacation(Vacation vacation, Teacher teacher) {
	jdbcTemplate.update(ASSIGN_VACATION, teacher.getId(), vacation.getStartDate(), vacation.getEndDate());
    }

    @Override
    public Optional<Teacher> findById(int id) {
	try {
	    Optional<Teacher> found = Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, teacherMapper, id));
	    return found;
	} catch (EmptyResultDataAccessException e) {
	    return Optional.empty();
	}
    }

    @Override
    public Optional<Teacher> findByAddressId(int id) {
	try {
	    return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ADDRESS_ID, teacherMapper, id));
	} catch (EmptyResultDataAccessException e) {
	    return Optional.empty();
	}
    }

    @Override
    public List<Teacher> findAll() {
	return jdbcTemplate.query(FIND_ALL, teacherMapper);
    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
