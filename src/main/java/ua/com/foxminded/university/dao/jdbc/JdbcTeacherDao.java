package ua.com.foxminded.university.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
public class JdbcTeacherDao implements TeacherDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTeacherDao.class);

    private static final String CREATE = "INSERT INTO teachers (first_name, last_name, gender, degree, "
            + "email, phone, address_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE teachers SET first_name = ?, last_name = ?, gender = ?, " +
            " degree = ?, email = ?, phone = ?, address_id = ? WHERE id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM teachers WHERE id = ?";
    private static final String FIND_BY_ADDRESS_ID = "SELECT * FROM teachers WHERE address_id = ?";
    private static final String FIND_BY_NAME_AND_EMAIL = "SELECT * FROM teachers WHERE first_name = ? AND last_name = ? AND email = ?";
    private static final String FIND_BY_SUBSTRING = "SELECT * FROM teachers WHERE lower(concat(first_name,' ',last_name)) like ?";
    private static final String DELETE_BY_ID = "DELETE FROM teachers WHERE id = ?";

    private static final String FIND_ALL_PAGEABLE = "SELECT * FROM teachers ORDER BY %s %s OFFSET ? FETCH FIRST ? ROWS ONLY";
    private static final String FIND_ALL = "SELECT * FROM teachers";
    private static final String COUNT_TOTAL = "SELECT COUNT(*) FROM teachers";

    private static final String REMOVE_SUBJECT = "DELETE FROM teachers_subjects where teacher_id = ? AND subject_id = ?";
    private static final String ASSIGN_SUBJECT = "INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (?, ?)";

    private static final String REMOVE_VACATION = "DELETE FROM vacations where teacher_id = ? AND start_date = ? AND end_date  = ?";
    private static final String ASSIGN_VACATION = "INSERT INTO vacations (teacher_id, start_date, end_date) VALUES (?, ?, ?)";

    private JdbcTemplate jdbcTemplate;
    private TeacherMapper teacherMapper;
    private AddressDao addressDao;

    @Value("${teacher.defaultsortattribute}")
    private String defaultSortAttribute;

    @Value("${defaultsortdirection}")
    private String defaultSortDirection;

    public JdbcTeacherDao(JdbcTemplate jdbcTemplate, TeacherMapper teacherMapper, AddressDao jdbcAddressDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherMapper = teacherMapper;
        this.addressDao = jdbcAddressDao;
    }

    @Override
    @Transactional
    public void create(Teacher teacher) {
        logger.debug("Writing a new teacher to database: {} ", teacher);
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

//        for (Vacation vacation : teacher.getVacations()) {
//            assignVacation(vacation, teacher);
//        }
    }

    @Override
    @Transactional
    public void update(Teacher teacher) {
        logger.debug("Updating teacher in database: {} ", teacher);
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
        jdbcTemplate.update(REMOVE_VACATION, teacher.getId(),
                vacation.getStartDate(),
                (vacation.getEndDate()));
    }

    private void assignVacation(Vacation vacation, Teacher teacher) {
        jdbcTemplate.update(ASSIGN_VACATION, teacher.getId(),
                vacation.getStartDate(),
                vacation.getEndDate());
    }

    @Override
    public Optional<Teacher> findById(int id) {
        logger.debug("Retrieving teacher by id:{} ", id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, teacherMapper, id));
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

    public Page<Teacher> findAll(Pageable pageable) {
        var sortProperty = defaultSortAttribute;
        var sortDirection = Sort.Direction.fromString(defaultSortDirection);

        var sortOrder = pageable.getSort().get().findFirst();
        if (sortOrder.isPresent()) {
            sortProperty = sortOrder.get().getProperty();
            sortDirection = sortOrder.get().getDirection();
        }

        logger.debug("Retrieving offset {}, size {}, sort {}", pageable.getOffset(), pageable.getPageSize(), pageable.getSort());

        var query = String.format(FIND_ALL_PAGEABLE, sortProperty, sortDirection);

        logger.debug("Using following query: {}", query);

        var teachers = jdbcTemplate.query(query, teacherMapper, pageable.getOffset(), pageable.getPageSize());
        var totalTeachers = jdbcTemplate.queryForObject(COUNT_TOTAL, Integer.class);

        return new PageImpl<>(teachers, pageable, totalTeachers);
    }

    @Override
    public List<Teacher> findAll() {
        logger.debug("Retrieving all teachers");
        return jdbcTemplate.query(FIND_ALL, teacherMapper);
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting teacher by id: {} ", id);
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public Optional<Teacher> findByNameAndEmail(String firstName, String lastName, String email) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_BY_NAME_AND_EMAIL, teacherMapper, firstName, lastName, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Teacher> findBySubstring(String substring) {
        String formattedSubstring="%"+substring.toLowerCase()+"%";
        logger.debug("Formatted search substring is {}", formattedSubstring);
        return jdbcTemplate.query(FIND_BY_SUBSTRING, teacherMapper, formattedSubstring);
    }
}
