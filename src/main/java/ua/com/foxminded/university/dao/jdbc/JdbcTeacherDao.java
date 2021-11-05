package ua.com.foxminded.university.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

//@Component
public class JdbcTeacherDao {

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

    private static final String REMOVE_SUBJECT = "DELETE FROM teachers_subjects WHERE teacher_id = ? AND subject_id = ?";
    private static final String ASSIGN_SUBJECT = "INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (?, ?)";

    private static final String REMOVE_VACATION = "DELETE FROM vacations WHERE teacher_id = ? AND start_date = ? AND end_date  = ?";
    private static final String ASSIGN_VACATION = "INSERT INTO vacations (teacher_id, start_date, end_date) VALUES (?, ?, ?)";
    private static final String FIND_REPLACEMENT = "SELECT * FROM teachers JOIN teachers_subjects ts " +
            "ON teachers.id = ts.teacher_id WHERE subject_id= ? AND teacher_id!= ?";

    private JdbcTemplate jdbcTemplate;

    private AddressDao addressDao;

    @Value("${teacher.defaultsortattribute}")
    private String defaultSortAttribute;

    @Value("${defaultsortdirection}")
    private String defaultSortDirection;

    public JdbcTeacherDao(JdbcTemplate jdbcTemplate, AddressDao jdbcAddressDao) {
        this.jdbcTemplate = jdbcTemplate;

        this.addressDao = jdbcAddressDao;
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









}
