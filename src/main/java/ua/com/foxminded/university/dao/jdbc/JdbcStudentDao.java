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
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.jdbc.mappers.StudentMapper;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcStudentDao implements StudentDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcStudentDao.class);

    private static final String CREATE = "INSERT INTO students (first_name, last_name, gender, birth_date," +
            " email, phone, address_id, group_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM students WHERE id = ?";
    private static final String FIND_BY_ADDRESS_ID = "SELECT * FROM students WHERE address_id = ?";
    private static final String FIND_BY_GROUP_ID = "SELECT * FROM students WHERE group_id = ?";
    private static final String FIND_BY_NAME_AND_BIRTH = "SELECT * FROM students WHERE first_name = ? AND last_name = ? AND birth_date = ?";
    private static final String UPDATE = "UPDATE students SET first_name = ?, last_name = ?, gender = ?, " +
            " birth_date = ?, email = ?, phone = ?, address_id = ?, group_id = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM students WHERE id = ?";
    private static final String FIND_ALL_PAGEABLE = "SELECT * FROM students ORDER BY %s %s OFFSET ? FETCH FIRST ? ROWS ONLY";
    private static final String FIND_ALL = "SELECT * FROM students";
    private static final String COUNT_IN_GROUP = "SELECT COUNT(*) FROM students WHERE group_id = ?";
    private static final String COUNT_TOTAL = "SELECT COUNT(*) FROM students";
    private static final String FIND_BY_SUBSTRING = "SELECT * FROM students WHERE lower(concat(first_name,' ',last_name)) like ?";

    private JdbcTemplate jdbcTemplate;
    private StudentMapper studentMapper;
    private AddressDao jdbcAddressDao;

    @Value("${student.defaultsortattribute}")
    private String defaultSortAttribute;

    @Value("${defaultsortdirection}")
    private String defaultSortDirection;

    public JdbcStudentDao(JdbcTemplate jdbcTemplate, StudentMapper studentMapper, AddressDao jdbcAddressDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentMapper = studentMapper;
        this.jdbcAddressDao = jdbcAddressDao;
    }

    @Override
    @Transactional
    public void create(Student student) {
        logger.debug("Writing a new student to database: {} ", student);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcAddressDao.create(student.getAddress());

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getGender().toString());
            ps.setObject(4, student.getBirthDate());
            ps.setString(5, student.getEmail());
            ps.setString(6, student.getPhoneNumber());
            ps.setInt(7, student.getAddress().getId());
            ps.setInt(8, student.getGroup().getId());
            return ps;
        }, keyHolder);
        student.setId((int) keyHolder.getKeys().get("id"));
    }

    @Override
    @Transactional
    public void update(Student student) {
        logger.debug("Updating student in database: {} ", student);
        var address = student.getAddress();
        jdbcAddressDao.update(address);

        jdbcTemplate.update(UPDATE, student.getFirstName(), student.getLastName(),
                student.getGender().toString(), student.getBirthDate(), student.getEmail(),
                student.getPhoneNumber(), student.getAddress().getId(),
                student.getGroup().getId(), student.getId());
    }

    @Override
    public Optional<Student> findById(int id) {
        logger.debug("Retrieving student by id: {} ", id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, studentMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Student> findByAddressId(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ADDRESS_ID, studentMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Student> findByNameAndBirthDate(String firstName, String lastName, LocalDate birthDate) {
        try {
            return Optional
                    .of(jdbcTemplate.queryForObject(FIND_BY_NAME_AND_BIRTH, studentMapper, firstName, lastName, birthDate));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Page<Student> findAll(Pageable pageable) {
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

        var students = jdbcTemplate.query(query, studentMapper, pageable.getOffset(), pageable.getPageSize());
        var totalStudents = jdbcTemplate.queryForObject(COUNT_TOTAL, Integer.class);

        return new PageImpl<>(students, pageable, totalStudents);
    }

    @Override
    public List<Student> findBySubstring(String substring) {
        String formattedSubstring = "%" + substring.toLowerCase() + "%";
        logger.debug("Formatted search substring is {}", formattedSubstring);
        return jdbcTemplate.query(FIND_BY_SUBSTRING, studentMapper, formattedSubstring);
    }

    @Override
    public List<Student> findAll() {
        logger.debug("Retrieving all students");
        return jdbcTemplate.query(FIND_ALL, studentMapper);
    }

    @Override
    public List<Student> findByGroup(Group group) {
        return jdbcTemplate.query(FIND_BY_GROUP_ID, studentMapper, group.getId());
    }

    @Override
    public int countInGroup(Group group) {
        return jdbcTemplate.queryForObject(COUNT_IN_GROUP, Integer.class, group.getId());
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting student by id: {} ", id);
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
