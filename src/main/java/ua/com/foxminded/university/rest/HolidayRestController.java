package ua.com.foxminded.university.rest;

import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.service.HolidayService;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
public class HolidayRestController {

    private final HolidayService holidayService;

    public HolidayRestController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public List<Holiday> findAll() {
        return holidayService.findAll();
    }

    @GetMapping("/{id}")
    public Holiday getHoliday(@PathVariable int id) {
        return holidayService.getById(id);
    }

    @PostMapping
    public void save(@RequestBody Holiday holiday) {
        holiday.setId(0);
        holidayService.create(holiday);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody Holiday holiday) {
        holiday.setId(id);
        holidayService.update(holiday);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        holidayService.delete(id);
    }
}