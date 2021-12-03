package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.service.HolidayService;

import javax.validation.Valid;
import javax.validation.ValidationException;

@Controller
@RequestMapping("/holidays")
public class HolidayController {

    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public String findAll(Model model) {
        logger.debug("Retrieving all holidays to controller");
        model.addAttribute("holidays", holidayService.findAll());
        return "holiday/all";
    }

    @GetMapping("/{id}")
    public String getHoliday(@PathVariable int id, Model model) {
        Holiday holiday = holidayService.getById(id);
        model.addAttribute("holiday", holiday);
        return "holiday/details";
    }

    @GetMapping("/new")
    public String showCreationForm(Holiday holiday) {
        logger.debug("Opening creation form");
        return "holiday/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("holiday") @Valid Holiday holiday, BindingResult result) {
        logger.debug("Create holiday={}", holiday);
        if(result.hasErrors()) {
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        holidayService.create(holiday);
        return "redirect:/holidays";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("holiday") @Valid Holiday holiday, BindingResult result) {
        logger.debug("Received update data: name {}", holiday.getName());
        if(result.hasErrors()) {
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        holidayService.update(holiday);
        return "redirect:/holidays";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        holidayService.delete(id);
        return "redirect:/holidays";
    }
}
