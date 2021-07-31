package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.university.service.ClassroomService;

@Controller
@RequestMapping("/classrooms")
public class ClassroomController {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomController.class);

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
	this.classroomService = classroomService;
    }

    @GetMapping
    public String findAll(Model model) {
	logger.debug("Retrieving all classrooms to controller");
	model.addAttribute("classrooms", classroomService.findAll());
	return "classroomsView";
    }
}
