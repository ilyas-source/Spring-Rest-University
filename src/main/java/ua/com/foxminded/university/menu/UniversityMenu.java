package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import java.awt.print.PrinterAbortException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcClassroomDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcHolidayDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcLectureDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcStudentDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcSubjectDAO;
import ua.com.foxminded.university.dao.jdbc.JdbcTeacherDAO;

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
    private JdbcTeacherDAO jdbcTeacherDAO;
    @Autowired
    private JdbcStudentDAO jdbcStudentDAO;
    @Autowired
    private JdbcSubjectDAO jdbcSubjectDAO;
    @Autowired
    private JdbcClassroomDAO jdbcClassroomDAO;
    @Autowired
    private JdbcGroupDAO jdbcGroupDAO;
    @Autowired
    private JdbcLectureDAO jdbcLectureDAO;
    @Autowired
    private JdbcHolidayDAO jdbcHolidayDAO;

    public void printUniversity() {
	StringBuilder result = new StringBuilder();
	result.append(FORMAT_DIVIDER);
	result.append("Teachers are:" + CR);
	result.append(teachersMenu.getStringOfTeachers(jdbcTeacherDAO.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Students are:" + CR);
	result.append(studentsMenu.getStringOfStudents(jdbcStudentDAO.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Current student groups:" + CR);
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
