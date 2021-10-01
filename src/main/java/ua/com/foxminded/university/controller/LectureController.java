package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.service.*;

import java.util.List;

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

    public LectureController(LectureService lectureService, GroupService groupService,
                             TimeslotService timeslotService, TeacherService teacherService,
                             ClassroomService classroomService, SubjectService subjectService) {
        this.lectureService = lectureService;
        this.groupService = groupService;
        this.timeslotService = timeslotService;
        this.teacherService = teacherService;
        this.classroomService = classroomService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public String findAll(Model model) {
        logger.debug("Retrieving all lectures to controller");
        model.addAttribute("lectures", lectureService.findAll());
        return "lecturesView";
    }

    @GetMapping("/{id}")
    public String showDetails(@PathVariable int id, Model model) {
        Lecture lecture = lectureService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find lecture by id " + id));
        model.addAttribute("lecture", lecture);
        model.addAttribute("allGroups", groupService.findAll());
        model.addAttribute("allTimeslots", timeslotService.findAll());
        model.addAttribute("allSubjects", subjectService.findAll());
        model.addAttribute("allClassrooms", classroomService.findAll());
        model.addAttribute("allTeachers", teacherService.findAll(PageRequest.of(0, 1000)));
        return "/details/lecture";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        logger.debug("Opening creation form");
        model.addAttribute("lecture", new Lecture());
        model.addAttribute("allGroups", groupService.findAll());
        model.addAttribute("allTimeslots", timeslotService.findAll());
        model.addAttribute("allSubjects", subjectService.findAll());
        model.addAttribute("allClassrooms", classroomService.findAll());
        model.addAttribute("allTeachers", teacherService.findAll(PageRequest.of(0, 1000)));
        return "/create/lecture";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("lecture") Lecture lecture) {
        logger.debug("Received to create: {}", lecture);
        Teacher teacher = teacherService.findById(lecture.getTeacher().getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find teacher by id " + lecture.getTeacher().getId()));
        Timeslot timeslot = timeslotService.findById(lecture.getTimeslot().getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find timeslot by id " + lecture.getTimeslot().getId()));
        Subject subject = subjectService.findById(lecture.getSubject().getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find subject by id " + lecture.getSubject().getId()));
        Classroom classroom = classroomService.findById(lecture.getClassroom().getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find classroom by id " + lecture.getClassroom().getId()));
        List<Group> groups=lecture.getGroups();
        logger.debug("Incomplete groups list is: {}", groups);
        logger.debug("0th is: {}", groups.get(0));
        for(int i=0;i<groups.size();i++) {
            logger.debug("Received {} groups to fill names", groups.size());
            int id=groups.get(i).getId();
            logger.debug("Retrieving {}th group", id);
            Group group = groupService.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("Can't find group by id " + id));
            logger.debug("Retrieved {}, setting name {}", group,group.getName());
            groups.get(i).setName(group.getName());
            logger.debug("Current groups list: {}", groups);
        }
        lecture.setTeacher(teacher);
        lecture.setTimeslot(timeslot);
        lecture.setSubject(subject);
        lecture.setClassroom(classroom);
        logger.debug("Full lecture: {}", lecture);
        lectureService.create(lecture);
        return "redirect:/lectures";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("lecture") Lecture lecture) {
        logger.debug("Received update data: {}", lecture);
        lectureService.update(lecture);
        return "redirect:/lectures";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        lectureService.delete(id);
        return "redirect:/lectures";
    }
}
