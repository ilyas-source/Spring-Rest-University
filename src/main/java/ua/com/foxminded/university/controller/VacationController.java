package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.service.VacationService;

import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/vacations")
public class VacationController {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

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
    public String addVacation(@ModelAttribute("classroom") Vacation vacation,
                              @PathVariable int id) {
        logger.debug("Received vacation to create: {}", vacation);
        logger.debug("Received teacher id to add vacation: {}", id);
        //int id = Integer.valueOf(teacherId);
        //LocalDate startDate=LocalDate.parse(vacation.getStartDate(),dateTimeFormatter);
        //LocalDate endDate=LocalDate.parse(end,dateTimeFormatter);

        Teacher teacher = teacherService.getById(id);
        vacationService.create(vacation);
        teacher.getVacations().add(vacation);
        teacherService.update(teacher);

        return "redirect:/teachers/editvacations/" + teacher.getId();
    }
}