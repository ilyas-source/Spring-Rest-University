package ua.com.foxminded.university.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
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
    private static final String FIND_ALL = "SELECT * FROM students";
    private static final String UPDATE = "UPDATE students SET first_name = ?, last_name = ?, gender = ?, " +
            " birth_date = ?, email = ?, phone = ?, address_id = ?, group_id = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM students WHERE id = ?";
    private static final String FIND_PAGE = "SELECT * FROM students WHERE id>=? ORDER BY id FETCH FIRST ? ROWS ONLY";

    private JdbcTemplate jdbcTemplate;
    private StudentMapper studentMapper;
    private AddressDao jdbcAddressDao;

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

        jdbcAddressDao.update(student.getAddress());

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

    @Override
    public List<Student> findPage(int startItem, int pageSize) {
        logger.debug("Retrieving students page, starting with pos.{}, items count {}",startItem,pageSize);
        return jdbcTemplate.query(FIND_PAGE, studentMapper, startItem, pageSize);
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
        return (int) findAll().stream().filter(s -> s.getGroup().equals(group)).count();
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting student by id: {} ", id);
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
