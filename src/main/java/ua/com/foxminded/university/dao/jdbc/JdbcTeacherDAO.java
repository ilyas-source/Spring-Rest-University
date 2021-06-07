package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.TeacherDAO;
import ua.com.foxminded.university.dao.jdbc.mappers.SubjectMapper;
import ua.com.foxminded.university.dao.jdbc.mappers.TeacherMapper;
import ua.com.foxminded.university.menu.SubjectsMenu;
import ua.com.foxminded.university.menu.TeachersMenu;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;

//CREATE TABLE teachers (
//id integer NOT NULL generated BY DEFAULT AS identity,
//first_name VARCHAR(255),
//last_name VARCHAR(255),
//gender GENDER,
//degree DEGREE,
//email VARCHAR(255),
//phone VARCHAR(255),
//address_id INTEGER REFERENCES addresses(id) ON DELETE CASCADE,
//CONSTRAINT teachers_pkey PRIMARY KEY (id)
//);

@Component
public class JdbcTeacherDAO implements TeacherDAO {

    private static final String CREATE = "INSERT INTO teachers (first_name, last_name, gender, degree, "
	    + "email, phone, address_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM teachers WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM teachers";
    private static final String UPDATE_ = "UPDATE teachers SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM teachers WHERE id = ?";

    private static final String FIND_ASSIGNED_SUBJECTS = "SELECT * FROM teachers_subjects WHERE teacher_id = ?";
    private static final String CLEAR_ASSIGNED_SUBJECTS = "DELETE FROM teachers_subjects WHERE teacher_id = ?";
    private static final String ASSIGN_SUBJECT_TO_TEACHER = "INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (?, ?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private JdbcAddressDAO jdbcAddressDAO;
    @Autowired
    private JdbcSubjectDAO jdbcSubjectDAO;
    @Autowired
    private JdbcVacationDAO jdbcVacationDAO;
    @Autowired
    private SubjectsMenu subjectsMenu;
    @Autowired
    private TeachersMenu teachersMenu;

    @Override
    public void addToDb(Teacher teacher) {
	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcAddressDAO.addToDb(teacher.getAddress());

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

	clearAssignedSubjects(teacher);
	for (Subject subject : teacher.getSubjects()) {
	    assignSubjectToTeacher(subject, teacher);
	}

//	System.out.print("Assigned subjects are: ");
//	System.out.println(subjectsMenu.getStringOfSubjects(findAssignedSubjects(teacher)));

    }
//
//    private List<Subject> findAssignedSubjects(Teacher teacher) {
//	return jdbcTemplate.query(FIND_ASSIGNED_SUBJECTS, subjectMapper, teacher.getId());
//    }

    private void clearAssignedSubjects(Teacher teacher) {
	jdbcTemplate.update(CLEAR_ASSIGNED_SUBJECTS, teacher.getId());
    }

    private void assignSubjectToTeacher(Subject subject, Teacher teacher) {
	jdbcTemplate.update(ASSIGN_SUBJECT_TO_TEACHER, teacher.getId(), subject.getId());
    }

    @Override
    public Optional<Teacher> findById(int id) {
	return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, teacherMapper, id));
    }

    @Override
    public List<Teacher> findAll() {
	return jdbcTemplate.query(FIND_ALL, teacherMapper);
    }

    @Override
    public void update(Teacher e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }

}
