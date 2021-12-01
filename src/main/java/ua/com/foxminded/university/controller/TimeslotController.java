package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.model.Timeslot;
import ua.com.foxminded.university.service.TimeslotService;

import javax.validation.Valid;
import javax.validation.ValidationException;

@Controller
@RequestMapping("/timeslots")
public class TimeslotController {

    private static final Logger logger = LoggerFactory.getLogger(TimeslotController.class);

    private final TimeslotService timeslotService;

    public TimeslotController(TimeslotService timeslotService) {
        this.timeslotService = timeslotService;
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("timeslot") @Valid Timeslot timeslot, BindingResult result) {
        logger.debug("Create timeslot={}", timeslot);
        if(result.hasErrors()) {
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        timeslotService.create(timeslot);
        return "redirect:/lectures";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("timeslot") @Valid Timeslot timeslot, BindingResult result) {
        logger.debug("Update timeslot={}", timeslot);
        if(result.hasErrors()) {
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        timeslotService.update(timeslot);
        return "redirect:/lectures";
    }
}