package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String getStudents(Model model, Pageable pageable) {
        logger.debug("Retrieving page {}, size {}, sort {}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        Page<Student> studentPage = studentService.findAll(pageable);
        model.addAttribute("studentPage", studentPage);

        return "studentsView";
    }

    @GetMapping("/{id}")
    public String showDetails(@PathVariable int id, Model model) {
        Student student = studentService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find student by id " + id));
        model.addAttribute("student", student);

        return "/details/student";
    }
}

