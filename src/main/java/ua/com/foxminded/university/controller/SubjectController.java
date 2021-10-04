package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Subject;
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
        return "subjectsView";
    }

    @GetMapping("/{id}")
    public String getSubject(@PathVariable int id, Model model) {
        Subject subject = subjectService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find subject by id " + id));
        model.addAttribute("subject", subject);
        return "/details/subject";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        logger.debug("Opening creation form");
        model.addAttribute("subject", new Subject());
        return "/create/subject";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("subject") Subject subject) {
        logger.debug("Received to create: {}", subject);
        subjectService.create(subject);
        return "redirect:/subjects";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("subject") Subject subject) {
        logger.debug("Received update data: name {}", subject.getName());
        subjectService.update(subject);
        return "redirect:/subjects";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        subjectService.delete(id);
        return "redirect:/subjects";
    }
}
