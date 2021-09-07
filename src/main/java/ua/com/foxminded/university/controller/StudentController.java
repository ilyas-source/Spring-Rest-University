package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        logger.debug("Retrieving page {} of size {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Student> studentPage = studentService.findAll(pageable);
        model.addAttribute("studentPage", studentPage);
        model.addAttribute("pageable", pageable);

        return "studentsView";
    }
}

