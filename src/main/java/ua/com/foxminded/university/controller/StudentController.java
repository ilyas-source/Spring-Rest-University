package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.StudentService;

import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Value("${page.defaultnumber}")
    private int defaultPageNumber;

    @Value("${page.defaultsize}")
    private int defaultPageSize;

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String getStudents(Model model, @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(defaultPageNumber);
        int pageSize = size.orElse(defaultPageSize);

        logger.debug("Retrieving students page {} of size {}", currentPage, pageSize);
        Page<Student> studentPage = studentService.findAll(PageRequest.of(currentPage-1, pageSize));
        model.addAttribute("studentPage", studentPage);

        return "studentsView";
    }
}

