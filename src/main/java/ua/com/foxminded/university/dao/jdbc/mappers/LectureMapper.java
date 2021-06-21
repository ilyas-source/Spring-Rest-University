package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDAO;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.TimeRange;

@Component
public class LectureMapper implements RowMapper<Lecture> {

    @Autowired
    JdbcSubjectDAO jdbcSubjectDAO;

    @Autowired
    JdbcTeacherDAO jdbcTeacherDAO;

    @Autowired
    JdbcClassroomDAO jdbcClassroomDAO;

    @Autowired
    JdbcGroupDAO jdbcGroupDAO;

    @Override
    public Lecture mapRow(ResultSet rs, int rowNum) throws SQLException {
	Lecture lecture = new Lecture();
	lecture.setId(rs.getInt("id"));

	Classroom classroom = jdbcClassroomDAO.findById(rs.getInt("classroom_id")).orElse(null);
	lecture.setClassroom(classroom);

	lecture.setDate(rs.getDate("date").toLocalDate());

	List<Group> groups = jdbcGroupDAO.findByLectureId(rs.getInt("id"));
	lecture.setGroups(groups);

	Subject subject = jdbcSubjectDAO.findById(rs.getInt("subject_id")).orElse(null);
	lecture.setSubject(subject);

	Teacher teacher = jdbcTeacherDAO.findById(rs.getInt("teacher_id")).orElse(null);
	lecture.setTeacher(teacher);

	LocalTime beginTime = rs.getTime("begin_time").toLocalTime();
	LocalTime endTime = rs.getTime("end_time").toLocalTime();
	lecture.setTime(new TimeRange(beginTime, endTime));

	return lecture;
    }
}
