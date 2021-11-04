package ua.com.foxminded.university.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.jdbc.mappers.StudentMapper;
import ua.com.foxminded.university.model.Student;

import java.util.List;

public class JdbcStudentDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcStudentDao.class);

    private static final String FIND_ALL_PAGEABLE = "SELECT * FROM students ORDER BY %s %s OFFSET ? FETCH FIRST ? ROWS ONLY";

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

//    public Optional<Student> findByAddressId(int id) {
//        try {
//            return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ADDRESS_ID, studentMapper, id));
//        } catch (EmptyResultDataAccessException e) {
//            return Optional.empty();
//        }
//    }

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

    public List<Student> findBySubstring(String substring) {
        String formattedSubstring = "%" + substring.toLowerCase() + "%";
        logger.debug("Formatted search substring is {}", formattedSubstring);
        return jdbcTemplate.query(FIND_BY_SUBSTRING, studentMapper, formattedSubstring);
    }
}
