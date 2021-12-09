package ua.com.foxminded.university.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Vacation;
import ua.com.foxminded.university.service.VacationService;

import java.util.List;

@RestController
@RequestMapping("/api/vacations")
public class VacationRestController {

    private final VacationService vacationService;

    public VacationRestController(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @GetMapping
    public List<Vacation> findAll() {
        return vacationService.findAll();
    }

    @GetMapping("/{id}")
    public Vacation getVacation(@PathVariable int id) {
        return vacationService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Vacation vacation) {
        vacationService.create(vacation);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody Vacation vacation) {
        vacation.setId(id);
        vacationService.update(vacation);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        vacationService.delete(id);
    }
}