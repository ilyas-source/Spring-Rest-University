package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.service.SubjectService;

import javax.validation.Valid;
import javax.validation.ValidationException;

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
        return "subject/all";
    }

    @GetMapping("/{id}")
    public String getSubject(@PathVariable int id, Model model) {
        Subject subject = subjectService.getById(id);
        model.addAttribute("subject", subject);
        return "subject/details";
    }

    @GetMapping("/new")
    public String showCreationForm(Subject subject) {
        logger.debug("Opening creation form");
        return "subject/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("subject") @Valid Subject subject, BindingResult result) {
        logger.debug("Create subject={}", subject);
        if(result.hasErrors()) {
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        subjectService.create(subject);
        return "redirect:/subjects";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("subject") @Valid Subject subject, BindingResult result) {
        logger.debug("Update subject={}", subject);
        if(result.hasErrors()) {
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        subjectService.update(subject);
        return "redirect:/subjects";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        subjectService.delete(id);
        return "redirect:/subjects";
    }
}
