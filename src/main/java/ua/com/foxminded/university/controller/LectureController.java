package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.service.*;

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
        model.addAttribute("allTeachers", teacherService.findAll(PageRequest.of(0,1000)));
        return "/details/lecture";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        logger.debug("Opening creation form");
        model.addAttribute("lecture", new Lecture());
        return "/create/lecture";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("lecture") Lecture lecture) {
        logger.debug("Received to create: {}", lecture);
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
