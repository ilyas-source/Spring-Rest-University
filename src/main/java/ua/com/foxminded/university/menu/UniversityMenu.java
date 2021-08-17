package ua.com.foxminded.university.menu;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.service.*;

import static ua.com.foxminded.university.Menu.CR;
import static ua.com.foxminded.university.Menu.FORMAT_DIVIDER;

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
    private TeacherService teacherService;
    private StudentService studentService;
    private SubjectService subjectService;
    private ClassroomService classroomService;
    private GroupService groupService;
    private LectureService lectureService;
    private HolidayService holidayService;
    private TimeslotService timeslotService;

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
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.subjectService = subjectService;
        this.classroomService = classroomService;
        this.groupService = groupService;
        this.lectureService = lectureService;
        this.holidayService = holidayService;
        this.timeslotService = timeslotService;
    }

    public void printUniversity() {
        StringBuilder result = new StringBuilder();
        result.append(FORMAT_DIVIDER);
        result.append("Teachers are:" + CR);
        result.append(teachersMenu.getStringOfTeachers(teacherService.findAll()));
        result.append(FORMAT_DIVIDER);
        result.append("Students are:" + CR);
        result.append(studentsMenu.getStringOfStudents(studentService.findAll()));
        result.append(FORMAT_DIVIDER);
        result.append("Student groups:" + CR);
        result.append(groupsMenu.getStringOfGroups(groupService.findAll()));
        result.append(FORMAT_DIVIDER);
        result.append("Subjects available to students:" + CR);
        result.append(subjectsMenu.getStringOfSubjects(subjectService.findAll()));
        result.append(FORMAT_DIVIDER);
        result.append("Classrooms list:" + CR);
        result.append(classroomsMenu.getStringOfClassrooms(classroomService.findAll()));
        result.append(FORMAT_DIVIDER);
        result.append("Time slots list:" + CR);
        result.append(timeslotsMenu.getStringOfTimeslots(timeslotService.findAll()));
        result.append(FORMAT_DIVIDER);
        result.append("Scheduled lectures:" + CR);
        result.append(lecturesMenu.getStringOfLectures(lectureService.findAll()));
        result.append(FORMAT_DIVIDER);
        result.append("Holidays are:" + CR);
        result.append(holidaysMenu.getStringOfHolidays(holidayService.findAll()));
        result.append(FORMAT_DIVIDER);
        System.out.println(result.toString());
    }
}
