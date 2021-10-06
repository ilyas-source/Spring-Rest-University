package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;
    private final GroupService groupService;

    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    public String getStudents(Model model, Pageable pageable) {
        logger.debug("Retrieving page {}, size {}, sort {}", pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort());
        Page<Student> studentPage = studentService.findAll(pageable);
        model.addAttribute("studentPage", studentPage);

        return "student/all";
    }

    @GetMapping("/{id}")
    public String getStudent(@PathVariable int id, Model model) {
        Student student = studentService.getById(id);
        model.addAttribute("student", student);
        model.addAttribute("groups", groupService.findAll());
        return "student/details";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model, Student student) {
        logger.debug("Opening creation form");
        model.addAttribute("groups", groupService.findAll());
        return "student/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("student") Student student) {
        logger.debug("Create student={}", student);
        studentService.create(student);
        return "redirect:/students";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("student") Student student) {
        logger.debug("Update student={}", student);
        studentService.update(student);
        return "redirect:/students";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        studentService.delete(id);
        return "redirect:/students";
    }
}

