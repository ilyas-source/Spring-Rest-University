package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.university.service.SubjectService;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
	this.subjectService = subjectService;
    }

    @GetMapping
    public String findAll(Model model) {
	logger.debug("Retrieving all subjects to controller");
	model.addAttribute("subjects", subjectService.findAll());
	return "subjects";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable int id, Model model) {
	logger.debug("Retrieving subject by id:{} to controller", id);
	model.addAttribute("subject", subjectService.findById(id).get());
	return "info/subject";
    }
}
