package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.service.HolidayService;

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
        return "holidaysView";
    }

    @GetMapping("/{id}")
    public String getHoliday(@PathVariable int id, Model model) {
        Holiday holiday = holidayService.getById(id);
        model.addAttribute("holiday", holiday);
        return "/details/holiday";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        logger.debug("Opening creation form");
        model.addAttribute("holiday", new Holiday());
        return "/create/holiday";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("holiday") Holiday holiday) {
        logger.debug("Received to create: {}", holiday);
        holidayService.create(holiday);
        return "redirect:/holidays";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("holiday") Holiday holiday) {
        logger.debug("Received update data: name {}", holiday.getName());
        holidayService.update(holiday);
        return "redirect:/holidays";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        holidayService.delete(id);
        return "redirect:/holidays";
    }
}
