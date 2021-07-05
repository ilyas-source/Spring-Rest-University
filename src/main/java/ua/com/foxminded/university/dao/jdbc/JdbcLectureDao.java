package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.jdbc.mappers.LectureMapper;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;

@Component
public class JdbcLectureDao implements LectureDao {

    private static final String CREATE = "INSERT INTO lectures (date, timeslot_id, subject_id, teacher_id, " +
	    "classroom_id) VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM lectures WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM lectures";
    private static final String UPDATE = "UPDATE lectures SET date = ?, timeslot_id = ?, " +
	    "subject_id = ?,  teacher_id = ?, classroom_id = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM lectures WHERE id = ?";

    private static final String REMOVE_GROUP = "DELETE FROM lectures_groups where lecture_id = ? AND group_id = ?";
    private static final String ASSIGN_GROUP = "INSERT INTO lectures_groups (lecture_id, group_id) VALUES (?, ?)";

    private JdbcTemplate jdbcTemplate;
    private LectureMapper lectureMapper;

    public JdbcLectureDao(JdbcTemplate jdbcTemplate, LectureMapper lectureMapper) {
	this.jdbcTemplate = jdbcTemplate;
	this.lectureMapper = lectureMapper;
    }

    @Override
    @Transactional
    public void create(Lecture lecture) {
	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcTemplate.update(connection -> {
	    PreparedStatement ps = connection
		    .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
	    ps.setObject(1, lecture.getDate());
	    ps.setObject(2, lecture.getTimeSlot().getId());
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
	jdbcTemplate.update(UPDATE, lecture.getDate(), lecture.getTimeSlot().getId(), lecture.getSubject().getId(),
		lecture.getTeacher().getId(), lecture.getClassroom().getId(), lecture.getId());

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

    public void assignGroupsToLecture(Lecture lecture) {
	List<Group> groups = lecture.getGroups();
	for (Group group : groups) {
	    jdbcTemplate.update(ASSIGN_GROUP, lecture.getId(), group.getId());
	}
    }
}
