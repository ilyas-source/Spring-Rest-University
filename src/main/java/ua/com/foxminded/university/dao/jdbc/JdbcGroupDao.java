package ua.com.foxminded.university.dao.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.dao.jdbc.mappers.GroupMapper;
import ua.com.foxminded.university.model.Group;

import java.util.List;

public class JdbcGroupDao  {

    private static final String FIND_BY_LECTURE_ID = "SELECT g.id, g.name from lectures_groups AS l_g LEFT JOIN groups AS g " +
            "ON (l_g.group_id=g.id) WHERE l_g.lecture_id = ?";

    private JdbcTemplate jdbcTemplate;
    private GroupMapper groupMapper;

    public JdbcGroupDao(JdbcTemplate jdbcTemplate, GroupMapper groupMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupMapper = groupMapper;
    }

    public List<Group> findByLectureId(int lectureId) {
        return jdbcTemplate.query(FIND_BY_LECTURE_ID, groupMapper, lectureId);
    }
}
