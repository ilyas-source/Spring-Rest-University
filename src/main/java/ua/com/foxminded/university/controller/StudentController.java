package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String findAll(Model model) {
	logger.debug("Retrieving all students to controller");
	model.addAttribute("students", studentService.findAll());
	return "students";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable int id, Model model) {
	logger.debug("Retrieving student by id:{} to controller", id);
	model.addAttribute("student", studentService.findById(id).get());
	return "info/student";
    }
}
