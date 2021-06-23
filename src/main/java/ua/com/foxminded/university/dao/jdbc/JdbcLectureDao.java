package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.jdbc.mappers.LectureMapper;
import ua.com.foxminded.university.model.Lecture;

@Component
public class JdbcLectureDao implements LectureDao {

    private static final String CREATE = "INSERT INTO lectures (date, begin_time, end_time, subject_id, teacher_id, " +
	    "classroom_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM lectures WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM lectures";
    private static final String UPDATE = "UPDATE lectures SET date = ?, begin_time = ?, end_time = ?, " +
	    "subject_id = ?,  teacher_id = ?, classroom_id = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM lectures WHERE id = ?";
    private static final String CLEAR_ASSIGNED_SUBJECTS = "DELETE FROM lectures_groups WHERE lecture_id = ?";

    private JdbcTemplate jdbcTemplate;
    private LectureMapper lectureMapper;
    private JdbcGroupDao jdbcGroupDao;

    public JdbcLectureDao(JdbcTemplate jdbcTemplate, LectureMapper lectureMapper, JdbcGroupDao jdbcGroupDao) {
	this.jdbcTemplate = jdbcTemplate;
	this.lectureMapper = lectureMapper;
	this.jdbcGroupDao = jdbcGroupDao;
    }

    @Override
    public void create(Lecture lecture) {
	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcTemplate.update(connection -> {
	    PreparedStatement ps = connection
		    .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
	    ps.setObject(1, lecture.getDate());
	    ps.setObject(2, lecture.getTime().getStartTime());
	    ps.setObject(3, lecture.getTime().getEndTime());
	    ps.setInt(4, lecture.getSubject().getId());
	    ps.setInt(5, lecture.getTeacher().getId());
	    ps.setInt(6, lecture.getClassroom().getId());
	    return ps;
	}, keyHolder);

	lecture.setId((int) keyHolder.getKeys().get("id"));

	jdbcGroupDao.assignGroupsToLecture(lecture);
    }

    @Override
    public void update(Lecture lecture) {
	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcTemplate.update(connection -> {
	    PreparedStatement ps = connection
		    .prepareStatement(UPDATE, Statement.NO_GENERATED_KEYS);
	    ps.setObject(1, lecture.getDate());
	    ps.setObject(2, lecture.getTime().getStartTime());
	    ps.setObject(3, lecture.getTime().getEndTime());
	    ps.setInt(4, lecture.getSubject().getId());
	    ps.setInt(5, lecture.getTeacher().getId());
	    ps.setInt(6, lecture.getClassroom().getId());
	    ps.setInt(7, lecture.getId());
	    return ps;
	}, keyHolder);

	clearAssignedSubjects(lecture);
	jdbcGroupDao.assignGroupsToLecture(lecture);
    }

    private void clearAssignedSubjects(Lecture lecture) {
	jdbcTemplate.update(CLEAR_ASSIGNED_SUBJECTS, lecture.getId());
    }

    @Override
    public Optional<Lecture> findById(int id) {
	try {
	    return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, lectureMapper, id));
	} catch (EmptyResultDataAccessException e) {
	    return Optional.empty();
	}
    }

    @Override
    public List<Lecture> findAll() {
	return jdbcTemplate.query(FIND_ALL, lectureMapper);
    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }

}
