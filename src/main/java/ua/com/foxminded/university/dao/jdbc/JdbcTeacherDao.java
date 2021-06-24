package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

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
    private static final String FIND_ALL = "SELECT * FROM teachers";
    private static final String DELETE_BY_ID = "DELETE FROM teachers WHERE id = ?";

    private static final String CLEAR_ASSIGNED_SUBJECTS = "DELETE FROM teachers_subjects WHERE teacher_id = ?";
    private static final String ASSIGN_SUBJECT = "INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (?, ?)";

    private static final String CLEAR_ASSIGNED_VACATIONS = "DELETE FROM vacations WHERE teacher_id = ?";
    private static final String ASSIGN_VACATION = "INSERT INTO vacations (teacher_id, start_date, end_date) VALUES (?, ?, ?)";

    private JdbcTemplate jdbcTemplate;
    private TeacherMapper teacherMapper;
    private JdbcAddressDao jdbcAddressDao;

    public JdbcTeacherDao(JdbcTemplate jdbcTemplate, TeacherMapper teacherMapper, JdbcAddressDao jdbcAddressDao) {
	this.jdbcTemplate = jdbcTemplate;
	this.teacherMapper = teacherMapper;
	this.jdbcAddressDao = jdbcAddressDao;
    }

    @Override
    public void create(Teacher teacher) {
	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcAddressDao.create(teacher.getAddress());

	jdbcTemplate.update(connection -> {
	    PreparedStatement ps = connection
		    .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
	    ps.setString(1, teacher.getFirstName());
	    ps.setString(2, teacher.getLastName());
	    ps.setObject(3, teacher.getGender(), java.sql.Types.OTHER);
	    ps.setObject(4, teacher.getDegree(), java.sql.Types.OTHER);
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
    public void update(Teacher teacher) {
	jdbcAddressDao.update(teacher.getAddress());

	jdbcTemplate.update(UPDATE, teacher.getFirstName(), teacher.getLastName(),
		new SqlParameterValue(java.sql.Types.OTHER, teacher.getGender()),
		new SqlParameterValue(java.sql.Types.OTHER, teacher.getDegree()),
		teacher.getEmail(), teacher.getPhoneNumber(), teacher.getAddress().getId(),
		teacher.getId());

	clearAssignedSubjects(teacher);
	for (Subject subject : teacher.getSubjects()) {
	    assignSubject(subject, teacher);
	}

	clearAssignedVacations(teacher);
	for (Vacation vacation : teacher.getVacations()) {
	    assignVacation(vacation, teacher);
	}
    }

//    private void clearAssignedSubjects(Teacher teacher) {
//	jdbcTemplate.update(CLEAR_ASSIGNED_SUBJECTS, teacher.getId());
//    }

    private void assignSubject(Subject subject, Teacher teacher) {
	jdbcTemplate.update(ASSIGN_SUBJECT, teacher.getId(), subject.getId());
    }

//    private void clearAssignedVacations(Teacher teacher) {
//	jdbcTemplate.update(CLEAR_ASSIGNED_VACATIONS, teacher.getId());
//    }

    private void assignVacation(Vacation vacation, Teacher teacher) {
	jdbcTemplate.update(ASSIGN_VACATION, teacher.getId(), vacation.getStartDate(), vacation.getEndDate());
    }

    @Override
    public Optional<Teacher> findById(int id) {
	try {
	    return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, teacherMapper, id));
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
