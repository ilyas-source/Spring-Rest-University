package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;
import ua.com.foxminded.university.service.SubjectService;
import ua.com.foxminded.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    private final TeacherService teacherService;
    private final SubjectService subjectService;


    public TeacherController(TeacherService teacherService, SubjectService subjectService) {
        this.teacherService = teacherService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public String getTeachers(Model model, Pageable pageable) {
        logger.debug("Retrieving page {}, size {}, sort {}", pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort());
        Page<Teacher> teacherPage = teacherService.findAll(pageable);
        model.addAttribute("teacherPage", teacherPage);
        return "teacher/all";
    }

    @GetMapping("/{id}")
    public String getTeacher(@PathVariable int id, Model model) {
        Teacher teacher = teacherService.getById(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("allSubjects", subjectService.findAll());
        return "teacher/details";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model, Teacher teacher) {
        logger.debug("Opening creation form");
        model.addAttribute("allSubjects", subjectService.findAll());
        return "teacher/create";
    }

    @GetMapping("/newvacation")
    public String showNewVacationForm(Teacher teacher) {
        logger.debug("Opening new vacation form");
        return "/teacher/vacations";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("teacher") Teacher teacher) {
        logger.debug("Received update data: {}", teacher);
        teacherService.update(teacher);
        return "redirect:/teachers";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("teacher") Teacher teacher) {
        logger.debug("Create teacher={}", teacher);
        teacherService.create(teacher);
        return "redirect:/teachers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        teacherService.delete(id);
        return "redirect:/teachers";
    }

    @GetMapping("/vacations/{id}")
    public String editVacations(@PathVariable int id, Model model, Vacation vacation) {
        logger.debug("Begin editing vacations for teacher id:{}", id);
        Teacher teacher = teacherService.getById(id);
        model.addAttribute(teacher);
        return "teacher/vacations";
    }
}
