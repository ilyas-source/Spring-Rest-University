package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import ua.com.foxminded.university.dao.jdbc.JdbcLectureDao;
import ua.com.foxminded.university.dao.jdbc.JdbcStudentDao;
import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import ua.com.foxminded.university.dao.jdbc.JdbcTimeslotDao;

@Component
public class UniversityMenu {

    private TeachersMenu teachersMenu;
    private ClassroomsMenu classroomsMenu;
    private StudentsMenu studentsMenu;
    private LecturesMenu lecturesMenu;
    private HolidaysMenu holidaysMenu;
    private GroupsMenu groupsMenu;
    private SubjectsMenu subjectsMenu;
    private TimeslotsMenu timeslotsMenu;
    private JdbcTeacherDao jdbcTeacherDao;
    private JdbcStudentDao jdbcStudentDao;
    private JdbcSubjectDao jdbcSubjectDao;
    private JdbcClassroomDao jdbcClassroomDao;
    private JdbcGroupDao jdbcGroupDao;
    private JdbcLectureDao jdbcLectureDao;
    private JdbcHolidayDao jdbcHolidayDao;
    private JdbcTimeslotDao jdbcTimeslotDao;

    public UniversityMenu(TeachersMenu teachersMenu, ClassroomsMenu classroomsMenu, StudentsMenu studentsMenu,
	    LecturesMenu lecturesMenu, HolidaysMenu holidaysMenu, GroupsMenu groupsMenu, SubjectsMenu subjectsMenu,
	    TimeslotsMenu timeslotsMenu, JdbcTeacherDao jdbcTeacherDao, JdbcStudentDao jdbcStudentDao,
	    JdbcSubjectDao jdbcSubjectDao, JdbcClassroomDao jdbcClassroomDao, JdbcGroupDao jdbcGroupDao,
	    JdbcLectureDao jdbcLectureDao, JdbcHolidayDao jdbcHolidayDao, JdbcTimeslotDao jdbcTimeslotDao) {
	this.teachersMenu = teachersMenu;
	this.classroomsMenu = classroomsMenu;
	this.studentsMenu = studentsMenu;
	this.lecturesMenu = lecturesMenu;
	this.holidaysMenu = holidaysMenu;
	this.groupsMenu = groupsMenu;
	this.subjectsMenu = subjectsMenu;
	this.timeslotsMenu = timeslotsMenu;
	this.jdbcTeacherDao = jdbcTeacherDao;
	this.jdbcStudentDao = jdbcStudentDao;
	this.jdbcSubjectDao = jdbcSubjectDao;
	this.jdbcClassroomDao = jdbcClassroomDao;
	this.jdbcGroupDao = jdbcGroupDao;
	this.jdbcLectureDao = jdbcLectureDao;
	this.jdbcHolidayDao = jdbcHolidayDao;
	this.jdbcTimeslotDao = jdbcTimeslotDao;
    }

    public void printUniversity() {
	StringBuilder result = new StringBuilder();
	result.append(FORMAT_DIVIDER);
	result.append("Teachers are:" + CR);
	result.append(teachersMenu.getStringOfTeachers(jdbcTeacherDao.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Students are:" + CR);
	result.append(studentsMenu.getStringOfStudents(jdbcStudentDao.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Student groups:" + CR);
	result.append(groupsMenu.getStringOfGroups(jdbcGroupDao.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Subjects available to students:" + CR);
	result.append(subjectsMenu.getStringOfSubjects(jdbcSubjectDao.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Classrooms list:" + CR);
	result.append(classroomsMenu.getStringOfClassrooms(jdbcClassroomDao.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Time slots list:" + CR);
	result.append(timeslotsMenu.getStringOfTimeslots(jdbcTimeslotDao.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Scheduled lectures:" + CR);
	result.append(lecturesMenu.getStringOfLectures(jdbcLectureDao.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Holidays are:" + CR);
	result.append(holidaysMenu.getStringOfHolidays(jdbcHolidayDao.findAll()));
	result.append(FORMAT_DIVIDER);
	System.out.println(result.toString());
    }
}
