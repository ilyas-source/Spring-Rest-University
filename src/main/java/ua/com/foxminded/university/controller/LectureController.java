package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.university.service.LectureService;

@Controller
@RequestMapping("/lectures")
public class LectureController {

    private static final Logger logger = LoggerFactory.getLogger(LectureController.class);

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
	this.lectureService = lectureService;
    }

    @GetMapping
    public String findAll(Model model) {
	logger.debug("Retrieving all lectures to controller");
	model.addAttribute("lectures", lectureService.findAll());
	return "lecturesView";
    }
}
