package ua.com.foxminded.university.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ua.com.foxminded.university.api.dto.ClassroomDto;
import ua.com.foxminded.university.api.mapper.ClassroomMapper;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.service.ClassroomService;
import ua.com.foxminded.university.service.LocationService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomRestController {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomRestController.class);
    private final ClassroomService classroomService;
    private final ClassroomMapper mapper;

    public ClassroomRestController(ClassroomService classroomService, LocationService locationService,
                                   ClassroomMapper mapper) {
        this.classroomService = classroomService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<Classroom> findAll() {
        return classroomService.findAll();
    }

    @GetMapping("/{id}")
    public Classroom classroom(@PathVariable int id) {
        return classroomService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Classroom> save(@RequestBody @Valid ClassroomDto classroomDto,
                                       UriComponentsBuilder builder) {
        Classroom classroom = mapper.classroomDtoToClassroom(classroomDto);
        classroomService.create(classroom);

        return created(builder.path("/classrooms/{id}").build(classroom.getId())).build();
    }

    @PutMapping("/{id}")
    public Classroom update(@PathVariable int id, @RequestBody @Valid ClassroomDto classroomDto) {
        Classroom classroom = mapper.classroomDtoToClassroom(classroomDto);
        classroom.setId(id);
        classroomService.update(classroom);

        return classroom;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        classroomService.delete(id);
    }
}