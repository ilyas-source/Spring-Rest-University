package ua.com.foxminded.university.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ua.com.foxminded.university.api.dto.HolidayDto;
import ua.com.foxminded.university.api.mapper.HolidayMapper;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.service.HolidayService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/holidays")
public class HolidayRestController {

    private final HolidayService holidayService;
    private final HolidayMapper mapper;

    public HolidayRestController(HolidayService holidayService, HolidayMapper mapper) {
        this.holidayService = holidayService;
        this.mapper = mapper;
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
    public ResponseEntity<Holiday> save(@RequestBody @Valid HolidayDto holidayDto,
                                        UriComponentsBuilder builder) {
        Holiday holiday = mapper.holidayDtoToHoliday(holidayDto);
        holidayService.create(holiday);

        return created(builder.path("/holidays/{id}").build(holiday.getId())).build();
    }

    @PutMapping("/{id}")
    public Holiday update(@PathVariable int id, @RequestBody @Valid HolidayDto holidayDto) {
        Holiday holiday = mapper.holidayDtoToHoliday(holidayDto);
        holiday.setId(id);
        holidayService.update(holiday);

        return holiday;
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        holidayService.delete(id);
    }
}