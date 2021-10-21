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

    @PostMapping("/create/{id}")
    public String addVacation(@ModelAttribute("vacation") Vacation vacation,
                              @PathVariable int id) {
        logger.debug("Create vacation={}", vacation);
        Teacher teacher = teacherService.getById(id);
        vacationService.create(vacation);
        teacher.getVacations().add(vacation);
        teacherService.update(teacher);
        return "redirect:/vacations/for/" + teacher.getId();
    }

    @GetMapping("/for/{id}")
    public String editVacations(@PathVariable int id, Model model, Vacation vacation) {
        logger.debug("Begin editing vacations for teacher id:{}", id);
        Teacher teacher = teacherService.getById(id);
        model.addAttribute(teacher);
        return "teacher/vacations";
    }
}