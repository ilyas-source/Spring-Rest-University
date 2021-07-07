package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.CR;
import static ua.com.foxminded.university.Menu.FORMAT_DIVIDER;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.ClassroomDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.HolidayDao;
import ua.com.foxminded.university.dao.LectureDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.SubjectDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.TimeslotDao;
import ua.com.foxminded.university.service.ClassroomService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.HolidayService;
import ua.com.foxminded.university.service.LectureService;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.university.service.SubjectService;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.service.TimeslotService;

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
    private TeacherService TeacherService;
    private StudentService StudentService;
    private SubjectService SubjectService;
    private ClassroomService ClassroomService;
    private GroupService GroupService;
    private LectureService LectureService;
    private HolidayService HolidayService;
    private TimeslotService TimeslotService;

    public UniversityMenu(TeachersMenu teachersMenu, ClassroomsMenu classroomsMenu, StudentsMenu studentsMenu,
	    LecturesMenu lecturesMenu, HolidaysMenu holidaysMenu, GroupsMenu groupsMenu, SubjectsMenu subjectsMenu,
	    TimeslotsMenu timeslotsMenu, TeacherService teacherService, StudentService studentService,
	    SubjectService subjectService, ClassroomService classroomService, GroupService groupService,
	    LectureService lectureService, HolidayService holidayService, TimeslotService timeslotService) {
	this.teachersMenu = teachersMenu;
	this.classroomsMenu = classroomsMenu;
	this.studentsMenu = studentsMenu;
	this.lecturesMenu = lecturesMenu;
	this.holidaysMenu = holidaysMenu;
	this.groupsMenu = groupsMenu;
	this.subjectsMenu = subjectsMenu;
	this.timeslotsMenu = timeslotsMenu;
	TeacherService = teacherService;
	StudentService = studentService;
	SubjectService = subjectService;
	ClassroomService = classroomService;
	GroupService = groupService;
	LectureService = lectureService;
	HolidayService = holidayService;
	TimeslotService = timeslotService;
    }

    public void printUniversity() {
	StringBuilder result = new StringBuilder();
	result.append(FORMAT_DIVIDER);
	result.append("Teachers are:" + CR);
	result.append(teachersMenu.getStringOfTeachers(TeacherService.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Students are:" + CR);
	result.append(studentsMenu.getStringOfStudents(StudentService.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Student groups:" + CR);
	result.append(groupsMenu.getStringOfGroups(GroupService.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Subjects available to students:" + CR);
	result.append(subjectsMenu.getStringOfSubjects(SubjectService.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Classrooms list:" + CR);
	result.append(classroomsMenu.getStringOfClassrooms(ClassroomService.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Time slots list:" + CR);
	result.append(timeslotsMenu.getStringOfTimeslots(TimeslotService.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Scheduled lectures:" + CR);
	result.append(lecturesMenu.getStringOfLectures(LectureService.findAll()));
	result.append(FORMAT_DIVIDER);
	result.append("Holidays are:" + CR);
	result.append(holidaysMenu.getStringOfHolidays(HolidayService.findAll()));
	result.append(FORMAT_DIVIDER);
	System.out.println(result.toString());
    }
}
