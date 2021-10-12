package ua.com.foxminded.university.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.jdbc.mappers.LectureMapper;
import ua.com.foxminded.university.model.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
public class JdbcLectureDao implements LectureDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcLectureDao.class);

    private static final String CREATE = "INSERT INTO lectures (date, timeslot_id, subject_id, teacher_id, " +
            "classroom_id) VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM lectures WHERE id = ?";
    private static final String FIND_BY_CLASSROOM_ID = "SELECT * FROM lectures WHERE classroom_id = ?";
    private static final String FIND_BY_TEACHER_ID = "SELECT * FROM lectures WHERE teacher_id = ?";
    private static final String FIND_BY_SUBJECT_ID = "SELECT * FROM lectures WHERE subject_id = ?";
    private static final String FIND_BY_TIMESLOT_ID = "SELECT * FROM lectures WHERE timeslot_id = ?";
    private static final String FIND_BY_DATE_TIME_CLASSROOM = "SELECT * FROM lectures WHERE date = ? AND timeslot_id = ? AND classroom_id = ?";
    private static final String FIND_BY_DATE_TIME_TEACHER = "SELECT * FROM lectures WHERE date = ? AND timeslot_id = ? AND teacher_id = ?";
    private static final String FIND_BY_DATE_TIME = "SELECT * FROM lectures WHERE date = ? AND timeslot_id = ?";
    private static final String FIND_ALL = "SELECT * FROM lectures";
    private static final String UPDATE = "UPDATE lectures SET date = ?, timeslot_id = ?, " +
            "subject_id = ?,  teacher_id = ?, classroom_id = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM lectures WHERE id = ?";

    private static final String REMOVE_GROUP = "DELETE FROM lectures_groups where lecture_id = ? AND group_id = ?";
    private static final String ASSIGN_GROUP = "INSERT INTO lectures_groups (lecture_id, group_id) VALUES (?, ?)";
    private static final String FIND_BY_TEACHER_AND_PERIOD = "SELECT * FROM lectures WHERE teacher_id = ? AND date >= ? AND date<= ?";
    private static final String FIND_BY_STUDENT_AND_PERIOD = "select * from lectures join lectures_groups lg" +
            " on lectures.id = lg.lecture_id where group_id=? AND date >= ? AND date<= ?";


    private JdbcTemplate jdbcTemplate;
    private LectureMapper lectureMapper;

    public JdbcLectureDao(JdbcTemplate jdbcTemplate, LectureMapper lectureMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lectureMapper = lectureMapper;
    }

    @Override
    @Transactional
    public void create(Lecture lecture) {
        logger.debug("Writing a new lecture to database: {} ", lecture);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, lecture.getDate());
            ps.setObject(2, lecture.getTimeslot().getId());
            ps.setInt(3, lecture.getSubject().getId());
            ps.setInt(4, lecture.getTeacher().getId());
            ps.setInt(5, lecture.getClassroom().getId());
            return ps;
        }, keyHolder);

        lecture.setId((int) keyHolder.getKeys().get("id"));

        assignGroupsToLecture(lecture);
    }

    @Override
    @Transactional
    public void update(Lecture lecture) {
        logger.debug("Updating lecture in database: {} ", lecture);
        jdbcTemplate.update(UPDATE, lecture.getDate(), lecture.getTimeslot().getId(),
                lecture.getSubject().getId(), lecture.getTeacher().getId(),
                lecture.getClassroom().getId(), lecture.getId());

        List<Group> newGroups = lecture.getGroups();
        List<Group> oldGroups = findById(lecture.getId()).get().getGroups();

        oldGroups.stream()
                .filter(not(newGroups::contains))
                .forEach(g -> removeGroup(g, lecture));
        newGroups.stream()
                .filter(not(oldGroups::contains))
                .forEach(g -> assignGroup(g, lecture));
    }

    private void removeGroup(Group group, Lecture lecture) {
        jdbcTemplate.update(REMOVE_GROUP, lecture.getId(), group.getId());
    }

    private void assignGroup(Group group, Lecture lecture) {
        jdbcTemplate.update(ASSIGN_GROUP, lecture.getId(), group.getId());
    }

    @Override
    public Optional<Lecture> findById(int id) {
        logger.debug("Retrieving lecture by id: {} ", id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, lectureMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Lecture> findAll() {
        logger.debug("Retrieving all lectures");
        return jdbcTemplate.query(FIND_ALL, lectureMapper);
    }

    @Override
    public List<Lecture> findByClassroom(Classroom classroom) {
        return jdbcTemplate.query(FIND_BY_CLASSROOM_ID, lectureMapper, classroom.getId());
    }

    @Override
    public List<Lecture> findByTeacher(Teacher teacher) {
        return jdbcTemplate.query(FIND_BY_TEACHER_ID, lectureMapper, teacher.getId());
    }

    @Override
    public List<Lecture> findBySubject(Subject subject) {
        return jdbcTemplate.query(FIND_BY_SUBJECT_ID, lectureMapper, subject.getId());
    }

    @Override
    public List<Lecture> findByTimeslot(Timeslot timeslot) {
        return jdbcTemplate.query(FIND_BY_TIMESLOT_ID, lectureMapper, timeslot.getId());
    }

    @Override
    public List<Lecture> findByTeacherAndPeriod(Teacher teacher, LocalDate start, LocalDate end) {
        return jdbcTemplate.query(FIND_BY_TEACHER_AND_PERIOD, lectureMapper, teacher.getId(), start, end);
    }

    @Override
    public List<Lecture> findByStudentAndPeriod(Student student, LocalDate start, LocalDate end) {
        return jdbcTemplate.query(FIND_BY_STUDENT_AND_PERIOD, lectureMapper, student.getGroup().getId(), start, end);

    }


    @Override
    public Optional<Lecture> findByDateTimeClassroom(LocalDate date, Timeslot timeslot, Classroom classroom) {
        try {
            return Optional.of(
                    jdbcTemplate.queryForObject(FIND_BY_DATE_TIME_CLASSROOM, lectureMapper,
                            date, timeslot.getId(), classroom.getId()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Lecture> findByDateTime(LocalDate date, Timeslot timeslot) {
        return jdbcTemplate.query(FIND_BY_DATE_TIME, lectureMapper, date, timeslot.getId());
    }

    public Optional<Lecture> findByDateTimeTeacher(LocalDate date, Timeslot timeslot, Teacher teacher) {
        try {
            return Optional.of(
                    jdbcTemplate.queryForObject(FIND_BY_DATE_TIME_TEACHER, lectureMapper, date,
                            timeslot.getId(), teacher.getId()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting lecture by id: {} ", id);
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    public void assignGroupsToLecture(Lecture lecture) {
        List<Group> groups = lecture.getGroups();
        for (Group group : groups) {
            jdbcTemplate.update(ASSIGN_GROUP, lecture.getId(), group.getId());
        }
    }
}
