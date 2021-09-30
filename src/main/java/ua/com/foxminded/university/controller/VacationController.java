package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.service.VacationService;

@Controller
@RequestMapping("/vacations")
public class VacationController {

    private static final Logger logger = LoggerFactory.getLogger(VacationController.class);

    private final VacationService vacationService;
    private final TeacherService teacherService;

    public VacationController(VacationService vacationService, TeacherService teacherService) {
        this.vacationService = vacationService;
        this.teacherService = teacherService;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        vacationService.delete(id);
        return "redirect:/teachers";
    }
}