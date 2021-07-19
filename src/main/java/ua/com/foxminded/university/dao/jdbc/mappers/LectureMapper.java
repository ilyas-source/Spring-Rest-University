package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.SubjectDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.TimeslotDao;
import ua.com.foxminded.university.model.Lecture;

@Component
public class LectureMapper implements RowMapper<Lecture> {

    private SubjectDao subjectDao;
    private TeacherDao teacherDao;
    private ClassroomDao classroomDao;
    private GroupDao groupDao;
    private TimeslotDao timeslotDao;

    public LectureMapper(SubjectDao subjectDao, TeacherDao teacherDao, ClassroomDao classroomDao,
	    GroupDao groupDao, TimeslotDao timeslotDao) {
	this.subjectDao = subjectDao;
	this.teacherDao = teacherDao;
	this.classroomDao = classroomDao;
	this.groupDao = groupDao;
	this.timeslotDao = timeslotDao;
    }

    @Override
    public Lecture mapRow(ResultSet rs, int rowNum) throws SQLException {
	var lecture = new Lecture();
	lecture.setId(rs.getInt("id"));
	classroomDao.findById(rs.getInt("classroom_id")).ifPresent(lecture::setClassroom);
	lecture.setDate(rs.getObject("date", LocalDate.class));
	lecture.setGroups(groupDao.findByLectureId(rs.getInt("id")));
	subjectDao.findById(rs.getInt("subject_id")).ifPresent(lecture::setSubject);
	teacherDao.findById(rs.getInt("teacher_id")).ifPresent(lecture::setTeacher);
	timeslotDao.findById(rs.getInt("timeslot_id")).ifPresent(lecture::setTimeslot);

	return lecture;
    }
}
