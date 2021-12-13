package ua.com.foxminded.university.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ua.com.foxminded.university.api.dto.VacationDto;
import ua.com.foxminded.university.api.mapper.VacationMapper;
import ua.com.foxminded.university.model.Vacation;
import ua.com.foxminded.university.service.VacationService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/vacations")
public class VacationRestController {

    private final VacationService vacationService;
    private final VacationMapper mapper;

    public VacationRestController(VacationService vacationService, VacationMapper mapper) {
        this.vacationService = vacationService;
        this.mapper = mapper;
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
    public ResponseEntity<Vacation> save(@RequestBody @Valid VacationDto vacationDto,
                                         UriComponentsBuilder builder) {
        Vacation vacation = mapper.vacationDtoToVacation(vacationDto);
        vacationService.create(vacation);

        return created(builder.path("/vacations/{id}").build(vacation.getId())).build();
    }

    @PutMapping("/{id}")
    public Vacation update(@PathVariable int id, @RequestBody @Valid VacationDto vacationDto) {
        Vacation vacation = mapper.vacationDtoToVacation(vacationDto);
        vacation.setId(id);
        vacationService.update(vacation);

        return vacation;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        vacationService.delete(id);
    }
}