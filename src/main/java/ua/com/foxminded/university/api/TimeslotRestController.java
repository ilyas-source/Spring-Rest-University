package ua.com.foxminded.university.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ua.com.foxminded.university.api.dto.TimeslotDto;
import ua.com.foxminded.university.api.mapper.TimeslotMapper;
import ua.com.foxminded.university.model.Timeslot;
import ua.com.foxminded.university.service.TimeslotService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/timeslots")
public class TimeslotRestController {

    private final TimeslotService timeslotService;
    private final TimeslotMapper mapper;

    public TimeslotRestController(TimeslotService timeslotService, TimeslotMapper mapper) {
        this.timeslotService = timeslotService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<Timeslot> findAll() {
        return timeslotService.findAll();
    }

    @GetMapping("/{id}")
    public Timeslot getTimeslot(@PathVariable int id) {
        return timeslotService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Timeslot> save(@RequestBody @Valid TimeslotDto timeslotDto,
                                         UriComponentsBuilder builder) {
        Timeslot timeslot = mapper.timeslotDtoToTimeslot(timeslotDto);
        timeslotService.create(timeslot);

        return created(builder.path("/timeslots/{id}").build(timeslot.getId())).build();
    }

    @PutMapping("/{id}")
    public Timeslot update(@PathVariable int id, @RequestBody @Valid TimeslotDto timeslotDto) {
        Timeslot timeslot = mapper.timeslotDtoToTimeslot(timeslotDto);
        timeslot.setId(id);
        timeslotService.update(timeslot);

        return timeslot;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        timeslotService.delete(id);
    }
}


