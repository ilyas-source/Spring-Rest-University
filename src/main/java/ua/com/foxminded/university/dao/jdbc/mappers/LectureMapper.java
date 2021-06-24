package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.TimeRange;

@Component
public class LectureMapper implements RowMapper<Lecture> {

    private JdbcSubjectDao jdbcSubjectDao;
    private JdbcTeacherDao jdbcTeacherDao;
    private JdbcClassroomDao jdbcClassroomDao;
    private JdbcGroupDao jdbcGroupDao;

    public LectureMapper(JdbcSubjectDao jdbcSubjectDao, JdbcTeacherDao jdbcTeacherDao, JdbcClassroomDao jdbcClassroomDao,
	    JdbcGroupDao jdbcGroupDao) {
	this.jdbcSubjectDao = jdbcSubjectDao;
	this.jdbcTeacherDao = jdbcTeacherDao;
	this.jdbcClassroomDao = jdbcClassroomDao;
	this.jdbcGroupDao = jdbcGroupDao;
    }

    @Override
    public Lecture mapRow(ResultSet rs, int rowNum) throws SQLException {
	Lecture lecture = new Lecture();
	lecture.setId(rs.getInt("id"));

	jdbcClassroomDao.findById(rs.getInt("classroom_id")).ifPresent(lecture::setClassroom);
	lecture.setDate(rs.getObject("date", LocalDate.class));
	lecture.setGroups(jdbcGroupDao.findByLectureId(rs.getInt("id")));
	jdbcSubjectDao.findById(rs.getInt("subject_id")).ifPresent(lecture::setSubject);
	jdbcTeacherDao.findById(rs.getInt("teacher_id")).ifPresent(lecture::setTeacher);

	LocalTime beginTime = rs.getTime("begin_time").toLocalTime();
	LocalTime endTime = rs.getTime("end_time").toLocalTime();
	lecture.setTime(new TimeRange(beginTime, endTime));

	return lecture;
    }
}
