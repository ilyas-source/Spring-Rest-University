package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
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

	Classroom classroom = jdbcClassroomDao.findById(rs.getInt("classroom_id")).orElseThrow();
	lecture.setClassroom(classroom);

	lecture.setDate(rs.getDate("date").toLocalDate());

	List<Group> groups = jdbcGroupDao.findByLectureId(rs.getInt("id"));
	lecture.setGroups(groups);

	Subject subject = jdbcSubjectDao.findById(rs.getInt("subject_id")).orElseThrow();
	lecture.setSubject(subject);

	Teacher teacher = jdbcTeacherDao.findById(rs.getInt("teacher_id")).orElseThrow();
	lecture.setTeacher(teacher);

	LocalTime beginTime = rs.getTime("begin_time").toLocalTime();
	LocalTime endTime = rs.getTime("end_time").toLocalTime();
	lecture.setTime(new TimeRange(beginTime, endTime));

	return lecture;
    }
}
