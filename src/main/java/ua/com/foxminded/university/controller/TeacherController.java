package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Teacher;
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
        return "teachersView";
    }

    @GetMapping("/{id}")
    public String showDetails(@PathVariable int id, Model model) {
        Teacher teacher = teacherService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find teacher by id " + id));
        model.addAttribute("teacher", teacher);
        model.addAttribute("allSubjects", subjectService.findAll());
        return "/details/teacher";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        logger.debug("Opening creation form");
        model.addAttribute("teacher", new Teacher());
        return "/create/teacher";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("teacher") Teacher teacher) {
        logger.debug("Received to create: {}", teacher);
        teacherService.create(teacher);
        return "redirect:/teachers";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("teacher") Teacher teacher) {
        logger.debug("Received update data: {}", teacher);
        teacherService.update(teacher);
        return "redirect:/teachers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        teacherService.delete(id);
        return "redirect:/teachers";
    }
}
