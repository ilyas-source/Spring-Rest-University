package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.*;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.SubjectDao;
import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.dao.TimeslotDao;
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
    private TeacherDao TeacherDao;
    private StudentDao StudentDao;
    private SubjectDao SubjectDao;
    private ClassroomDao ClassroomDao;
    private GroupDao GroupDao;
    private LectureDao LectureDao;
    private HolidayDao HolidayDao;
    private TimeslotDao TimeslotDao;

    public UniversityMenu(TeachersMenu teachersMenu, ClassroomsMenu classroomsMenu, StudentsMenu studentsMenu,
	    LecturesMenu lecturesMenu, HolidaysMenu holidaysMenu, GroupsMenu groupsMenu, SubjectsMenu subjectsMenu,
	    TimeslotsMenu timeslotsMenu, TeacherDao TeacherDao, StudentDao StudentDao,
	    JdbcSubjectDao SubjectDao, ClassroomDao ClassroomDao, JdbcGroupDao GroupDao,
	    LectureDao LectureDao, JdbcHolidayDao HolidayDao, JdbcTimeslotDao TimeslotDao) {
	this.teachersMenu = teachersMenu;
	this.classroomsMenu = classroomsMenu;
	this.studentsMenu = studentsMenu;
	this.lecturesMenu = lecturesMenu;
	this.holidaysMenu = holidaysMenu;
	this.groupsMenu = groupsMenu;
	this.subjectsMenu = subjectsMenu;
	this.timeslotsMenu = timeslotsMenu;
	this.TeacherDao = TeacherDao;
	this.StudentDao = StudentDao;
	this.SubjectDao = SubjectDao;
	this.ClassroomDao = ClassroomDao;
	this.GroupDao = GroupDao;
	this.LectureDao = LectureDao;
	this.HolidayDao = HolidayDao;
	this.TimeslotDao = TimeslotDao;
    }

    public void printUniversity() {
	StringBuilder result = new StringBuilder();
	result.append(FORMAT_DIVIDER);
	result.append("Teachers are:" + CR);
	result.append(teachersMenu.getStringOfTeachers(TeacherDao.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Students are:" + CR);
	result.append(studentsMenu.getStringOfStudents(StudentDao.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Student groups:" + CR);
	result.append(groupsMenu.getStringOfGroups(GroupDao.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Subjects available to students:" + CR);
	result.append(subjectsMenu.getStringOfSubjects(SubjectDao.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Classrooms list:" + CR);
	result.append(classroomsMenu.getStringOfClassrooms(ClassroomDao.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Time slots list:" + CR);
	result.append(timeslotsMenu.getStringOfTimeslots(TimeslotDao.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Scheduled lectures:" + CR);
	result.append(lecturesMenu.getStringOfLectures(LectureDao.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Holidays are:" + CR);
	result.append(holidaysMenu.getStringOfHolidays(HolidayDao.findAll()));
	result.append(FORMAT_DIVIDER);
	System.out.println(result.toString());
    }
}
