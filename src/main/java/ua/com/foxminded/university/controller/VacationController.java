package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;
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

    @GetMapping("/newvacation/{id}")
    public String showNewVacationForm(@PathVariable int id) {
        logger.debug("Opening new vacation form");
        return "/teacher/vacations";
    }

    @PostMapping("/create/{id}")
    public String addVacation(@ModelAttribute("classroom") Vacation vacation,
                              @PathVariable int id) {
        logger.debug("Create vacation={}", vacation);
        Teacher teacher = teacherService.getById(id);
        vacationService.create(vacation);
        teacher.getVacations().add(vacation);
        teacherService.update(teacher);
        return "redirect:/teachers/vacations/" + teacher.getId();
    }

    @GetMapping("/for/{id}")
    public String editVacations(@PathVariable int id, Model model, Vacation vacation) {
        logger.debug("Begin editing vacations for teacher id:{}", id);
        Teacher teacher = teacherService.getById(id);
        model.addAttribute(teacher);
        return "teacher/vacations";
    }
}