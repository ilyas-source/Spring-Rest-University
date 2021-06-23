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

import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.jdbc.mappers.GroupMapper;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;

@Component
public class JdbcGroupDao implements GroupDao {

    private static final String CREATE = "INSERT INTO groups (name) VALUES (?)";
    private static final String FIND_BY_ID = "SELECT * FROM groups WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM groups";
    private static final String UPDATE = "UPDATE groups SET name = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM groups WHERE id = ?";
    private static final String ASSIGN_GROUP_TO_LECTURE = "INSERT INTO lectures_groups (lecture_id, group_id) VALUES (?, ?)";
    private static final String FIND_BY_LECTURE_ID = "SELECT g.id, g.name from lectures_groups AS l_g LEFT JOIN groups AS g " +
	    "ON (l_g.group_id=g.id) WHERE l_g.lecture_id = ?";

    private JdbcTemplate jdbcTemplate;
    private GroupMapper groupMapper;

    public JdbcGroupDao(JdbcTemplate jdbcTemplate, GroupMapper groupMapper) {
	this.jdbcTemplate = jdbcTemplate;
	this.groupMapper = groupMapper;
    }

    @Override
    public void addToDb(Group group) {
	List<Group> groups = findAll();
	if (!groups.contains(group)) {
	    KeyHolder keyHolder = new GeneratedKeyHolder();

	    jdbcTemplate.update(connection -> {
		PreparedStatement ps = connection
			.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, group.getName());
		return ps;
	    }, keyHolder);
	    group.setId((int) keyHolder.getKeys().get("id"));
	}

    }

    public List<Group> findByLectureId(int lectureId) {
	return jdbcTemplate.query(FIND_BY_LECTURE_ID, groupMapper, lectureId);
    }

    @Override
    public Optional<Group> findById(int id) {
	try {
	    return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, groupMapper, id));
	} catch (EmptyResultDataAccessException e) {
	    return Optional.empty();
	}
    }

    @Override
    public List<Group> findAll() {
	return jdbcTemplate.query(FIND_ALL, groupMapper);
    }

    @Override
    public void update(Group group) {
	jdbcTemplate.update(UPDATE, group.getName(), group.getId());
    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }

    public void assignGroupsToLecture(Lecture lecture) {
	List<Group> groups = lecture.getGroups();
	for (Group group : groups) {
	    jdbcTemplate.update(ASSIGN_GROUP_TO_LECTURE, lecture.getId(), group.getId());
	}
    }
}