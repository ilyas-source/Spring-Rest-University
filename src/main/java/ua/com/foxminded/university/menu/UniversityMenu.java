package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDao;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import ua.com.foxminded.university.dao.jdbc.JdbcLectureDao;
import ua.com.foxminded.university.dao.jdbc.JdbcStudentDao;
import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDao;

@Component
public class UniversityMenu {

    @Autowired
    private TeachersMenu teachersMenu;
    @Autowired
    private ClassroomsMenu classroomsMenu;
    @Autowired
    private StudentsMenu studentsMenu;
    @Autowired
    private LecturesMenu lecturesMenu;
    @Autowired
    private HolidaysMenu holidaysMenu;
    @Autowired
    private GroupsMenu groupsMenu;
    @Autowired
    private SubjectsMenu subjectsMenu;

    @Autowired
    private JdbcTeacherDao jdbcTeacherDAO;
    @Autowired
    private JdbcStudentDao jdbcStudentDAO;
    @Autowired
    private JdbcSubjectDao jdbcSubjectDAO;
    @Autowired
    private JdbcClassroomDao jdbcClassroomDAO;
    @Autowired
    private JdbcGroupDao jdbcGroupDAO;
    @Autowired
    private JdbcLectureDao jdbcLectureDAO;
    @Autowired
    private JdbcHolidayDao jdbcHolidayDAO;

    public void printUniversity() {
	StringBuilder result = new StringBuilder();
	result.append(FORMAT_DIVIDER);
	result.append("Teachers are:" + CR);
	result.append(teachersMenu.getStringOfTeachers(jdbcTeacherDAO.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Students are:" + CR);
	result.append(studentsMenu.getStringOfStudents(jdbcStudentDAO.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Student groups:" + CR);
	result.append(groupsMenu.getStringOfGroups(jdbcGroupDAO.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Subjects available to students:" + CR);
	result.append(subjectsMenu.getStringOfSubjects(jdbcSubjectDAO.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Classrooms list:" + CR);
	result.append(classroomsMenu.getStringOfClassrooms(jdbcClassroomDAO.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Scheduled lectures:" + CR);
	result.append(lecturesMenu.getStringOfLectures(jdbcLectureDAO.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Holidays are:" + CR);
	result.append(holidaysMenu.getStringOfHolidays(jdbcHolidayDAO.findAll()));
	result.append(FORMAT_DIVIDER);
	System.out.println(result.toString());
    }
}
