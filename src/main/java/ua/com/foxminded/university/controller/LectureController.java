package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.service.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/lectures")
public class LectureController {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
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

    @GetMapping("/schedule")
    public String findSchedule(@RequestParam("entity") String entity,
                               @RequestParam("duration") String duration,
                               @RequestParam("id") String personId,
                               @RequestParam("date") String date,
                               Model model) {
        logger.debug("Received schedule parameters to retrieve: {} with id:{}, for {}, date:{}",
                                entity, personId, duration, date);
        LocalDate scheduleDate = LocalDate.parse(date, dateTimeFormatter);
        int id = Integer.parseInt(personId);
        List<Lecture> schedule = new ArrayList<>();
        if (entity.equals("teacher")) {
            if (duration.equals("day")) {
                logger.debug("Retrieving schedule for teacher and day");
                schedule = lectureService.findByTeacherAndDay(teacherService.getById(id), scheduleDate);
            } else {
                logger.debug("Retrieving schedule for teacher and month");
                schedule = lectureService.findByTeacherAndMonth(teacherService.getById(id), scheduleDate);
            }
        }
        if (entity.equals("student")) {
            if (duration.equals("day")) {
                logger.debug("Retrieving schedule for student and day");
                schedule = lectureService.findByStudentAndDay(studentService.getById(id), scheduleDate);
            } else {
                logger.debug("Retrieving schedule for student and month");
                schedule = lectureService.findByStudentAndMonth(studentService.getById(id), scheduleDate);
            }
        }
        model.addAttribute("lectures", schedule);
        return "lecture/all";
    }

    @GetMapping("/{id}")
    public String getLecture(@PathVariable int id, Model model) {
        Lecture lecture = lectureService.getById(id);
        model.addAttribute("lecture", lecture);
        model.addAttribute("allGroups", groupService.findAll());
        model.addAttribute("allTimeslots", timeslotService.findAll());
        model.addAttribute("allSubjects", subjectService.findAll());
        model.addAttribute("allClassrooms", classroomService.findAll());
        model.addAttribute("allTeachers", teacherService.findAll(PageRequest.of(0, 1000)));
        return "lecture/details";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model, Lecture lecture) {
        logger.debug("Opening creation form");
        model.addAttribute("allGroups", groupService.findAll());
        model.addAttribute("allTimeslots", timeslotService.findAll());
        model.addAttribute("allSubjects", subjectService.findAll());
        model.addAttribute("allClassrooms", classroomService.findAll());
        model.addAttribute("allTeachers", teacherService.findAll(PageRequest.of(0, 1000)));
        return "lecture/create";
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

        List<Group> groups = lecture.getGroups();
        for (Group value : groups) {
            int id = Integer.parseInt(value.getName());
            Group group = groupService.getById(id);
            value.setId(id);
            value.setName(group.getName());
        }
        logger.debug("Full lecture: {}", lecture);
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        lectureService.delete(id);
        return "redirect:/lectures";
    }
}
