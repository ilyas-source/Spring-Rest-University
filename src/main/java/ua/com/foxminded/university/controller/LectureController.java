package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.service.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/lectures")
public class LectureController {

    private static final Logger logger = LoggerFactory.getLogger(LectureController.class);

    private final LectureService lectureService;
    private final GroupService groupService;
    private final TimeslotService timeslotService;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;
    private final SubjectService subjectService;
    private final StudentService studentService;

    public LectureController(LectureService lectureService, GroupService groupService,
                             TimeslotService timeslotService, TeacherService teacherService,
                             ClassroomService classroomService, SubjectService subjectService,
                             StudentService studentService) {
        this.lectureService = lectureService;
        this.groupService = groupService;
        this.timeslotService = timeslotService;
        this.teacherService = teacherService;
        this.classroomService = classroomService;
        this.subjectService = subjectService;
        this.studentService = studentService;
    }

    @GetMapping
    public String findAll(Model model) {
        logger.debug("Retrieving all lectures to controller");
        model.addAttribute("lectures", lectureService.findAll());
        return "lecture/all";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model, Lecture lecture) {
        logger.debug("Opening creation form");
        model.addAttribute("allGroups", groupService.findAll());
        model.addAttribute("allTimeslots", timeslotService.findAll());
        model.addAttribute("allSubjects", subjectService.findAll());
        model.addAttribute("allClassrooms", classroomService.findAll());
        model.addAttribute("allTeachers", teacherService.findAll());
        return "lecture/create";
    }

    @GetMapping("/search")
    public String searchPerson(@RequestParam String entity,
                               @RequestParam String substring,
                               Model model) {
        logger.debug("Searching for {} with substring {}", entity, substring);
        List<Student> students = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();

        if (entity.equals("student")) {
            students = studentService.findBySubstring(substring);
        } else {
            teachers = teacherService.findBySubstring(substring);
        }

        model.addAttribute("students", students);
        model.addAttribute("teachers", teachers);
        return "universityView";
    }

    @GetMapping("/schedule")
    public String showScheduleView(@RequestParam("id") int personId,
                                   @RequestParam String entity,
                                   @RequestParam String periodType,
                                   @DateTimeFormat(pattern = "dd.MM.yyyy") @RequestParam LocalDate date,
                                   Model model) {
        logger.debug("Received for schedule: {} with id:{}, for {}, with date {}", entity, personId, periodType, date);
        model.addAttribute("entity", entity);
        model.addAttribute("periodType", periodType);
        model.addAttribute("date", date);
        model.addAttribute("id", personId);
        var teacher = new Teacher();
        var student = new Student();
        var person = "";
        if (entity.equals("teacher")) {
            teacher = teacherService.getById(personId);
            person = "teacher " + teacher.getFirstName() + " " + teacher.getLastName();
        } else {
            student = studentService.getById(personId);
            person = "student " + student.getFirstName() + " " + student.getLastName();
        }
        model.addAttribute("personName", person);

        return "calendar";
    }

    @GetMapping("/schedule/calendar")
    @ResponseBody
    public List<Lecture> retrieveLecturesForCalendar(@RequestParam int id,
                                                     @RequestParam String entity,
                                                     @RequestParam("start")
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                             ZonedDateTime startTime,
                                                     @RequestParam("end")
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                             ZonedDateTime endTime) {
        logger.debug("Calendar retrieves lectures for {} id:{} from {} to {}", entity, id, startTime, endTime);
        List<Lecture> result;
        if (entity.equals("teacher")) {
            var teacher = teacherService.getById(id);
            result = lectureService.findByTeacherAndPeriod(teacher, startTime.toLocalDate(), endTime.toLocalDate());
        } else {
            var student = studentService.getById(id);
            result = lectureService.findByStudentAndPeriod(student, startTime.toLocalDate(), endTime.toLocalDate());
        }
        return result;
    }

    @GetMapping("/{id}")
    public String getLecture(@PathVariable int id, Model model) {
        Lecture lecture = lectureService.getById(id);
        model.addAttribute("lecture", lecture);
        model.addAttribute("allGroups", groupService.findAll());
        model.addAttribute("allTimeslots", timeslotService.findAll());
        model.addAttribute("allSubjects", subjectService.findAll());
        model.addAttribute("allClassrooms", classroomService.findAll());
        model.addAttribute("allTeachers", teacherService.findAll());

        return "lecture/details";
    }

    @GetMapping("/replacement")
    public String showReplacementView(Model model) {
        model.addAttribute("allTeachers", teacherService.findAll());
        return "lecture/replacement";
    }

    @PostMapping("/replacement")
    public String replaceTeacher(@RequestParam("teacher") int id,
                                 @RequestParam("start") LocalDate start,
                                 @RequestParam("end") LocalDate end) {
        logger.debug("Teacher replacement: id:{}, from {} to {}", id, start, end);
        Teacher teacher = teacherService.getById(id);
        lectureService.replaceTeacher(teacher, start, end);
        return "redirect:/lectures";
    }


    @PostMapping("/create")
    public String create(@ModelAttribute("lecture") Lecture lecture) {
        logger.debug("Create lecture={}", lecture);
        refreshFieldsFromDatabase(lecture);
        lectureService.create(lecture);
        return "redirect:/lectures";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("lecture") Lecture lecture) {
        logger.debug("Update lecture={}", lecture);
        refreshFieldsFromDatabase(lecture);
        lectureService.update(lecture);
        return "redirect:/lectures";
    }

    private void refreshFieldsFromDatabase(@ModelAttribute("lecture") Lecture lecture) {
        Teacher teacher = teacherService.getById(lecture.getTeacher().getId());
        Timeslot timeslot = timeslotService.getById(lecture.getTimeslot().getId());
        Subject subject = subjectService.getById(lecture.getSubject().getId());
        Classroom classroom = classroomService.getById(lecture.getClassroom().getId());

        lecture.setTeacher(teacher);
        lecture.setTimeslot(timeslot);
        lecture.setSubject(subject);
        lecture.setClassroom(classroom);

        Set<Group> groups = lecture.getGroups();
        for (Group group : groups) {
            logger.debug("Received group {}, with id {}, name {}", group, group.getId(), group.getName());
            group.setName(groupService.getById(group.getId()).getName());
        }
        logger.debug("Full lecture: {}", lecture);
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        lectureService.delete(id);
        return "redirect:/lectures";
    }
}
