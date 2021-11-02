package ua.com.foxminded.university.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxminded.university.dao.jdbc.mappers.LectureMapper;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Student;

import java.time.LocalDate;
import java.util.List;

public class JdbcLectureDao {

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
