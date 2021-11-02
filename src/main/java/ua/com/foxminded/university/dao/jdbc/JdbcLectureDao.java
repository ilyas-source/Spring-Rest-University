package ua.com.foxminded.university.dao.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.dao.jdbc.mappers.LectureMapper;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Student;

import java.time.LocalDate;
import java.util.List;

public class JdbcLectureDao {

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

    private void removeGroup(Group group, Lecture lecture) {
        jdbcTemplate.update(REMOVE_GROUP, lecture.getId(), group.getId());
    }

    private void assignGroup(Group group, Lecture lecture) {
        jdbcTemplate.update(ASSIGN_GROUP, lecture.getId(), group.getId());
    }

    public List<Lecture> findByStudentAndPeriod(Student student, LocalDate start, LocalDate end) {
        return jdbcTemplate.query(FIND_BY_STUDENT_AND_PERIOD, lectureMapper, student.getGroup().getId(), start, end);
    }

    public void assignGroupsToLecture(Lecture lecture) {
        List<Group> groups = lecture.getGroups();
        for (Group group : groups) {
            jdbcTemplate.update(ASSIGN_GROUP, lecture.getId(), group.getId());
        }
    }
}
