package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.service.*;

import java.util.List;

import static java.lang.Integer.valueOf;

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
    public String getLecture(@PathVariable int id, Model model) {
        Lecture lecture = lectureService.getById(id);
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
        refreshFieldsFromDatabase(lecture);
        lectureService.create(lecture);
        return "redirect:/lectures";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("lecture") Lecture lecture) {
        logger.debug("Received update data: {}", lecture);
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
        for (int i = 0; i < groups.size(); i++) {
            int id = valueOf(groups.get(i).getName());
            Group group = groupService.getById(id);
            groups.get(i).setId(id);
            groups.get(i).setName(group.getName());
        }
        logger.debug("Full lecture: {}", lecture);
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        lectureService.delete(id);
        return "redirect:/lectures";
    }
}
