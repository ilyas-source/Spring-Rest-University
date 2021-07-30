package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
	this.teacherService = teacherService;
    }

    @GetMapping
    public String findAll(Model model) {
	logger.debug("Retrieving all teachers to controller");
	model.addAttribute("teachers", teacherService.findAll());
	return "teachers";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable int id, Model model) {
	logger.debug("Retrieving teacher by id:{} to controller", id);
	model.addAttribute("teacher", teacherService.findById(id).get());
	return "info/teacher";
    }
}
