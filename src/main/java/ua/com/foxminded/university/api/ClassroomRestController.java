package ua.com.foxminded.university.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ua.com.foxminded.university.api.dto.ClassroomDto;
import ua.com.foxminded.university.api.dto.mapper.ClassroomMapper;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.service.ClassroomService;
import ua.com.foxminded.university.service.LocationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomRestController {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomRestController.class);

    private final ClassroomService classroomService;


    public ClassroomRestController(ClassroomService classroomService, LocationService locationService) {
        this.classroomService = classroomService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Classroom> findAll() {
        return classroomService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Classroom classroom(@PathVariable int id) {
        return classroomService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Classroom> save(@RequestBody @Valid ClassroomDto classroomDto,
                                          UriComponentsBuilder builder) {
        Classroom classroom = ClassroomMapper.INSTANCE.classroomDtoToClassroom(classroomDto);
        classroomService.create(classroom);

        HttpHeaders headers = new HttpHeaders();
        String location = builder.path("/classrooms/{id}")
                .buildAndExpand(classroom.getId()).toUriString();
        headers.add("location", location);

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Classroom> update(@PathVariable int id, @RequestBody @Valid ClassroomDto classroomDto,
                                            UriComponentsBuilder builder) {
        Classroom classroom = ClassroomMapper.INSTANCE.classroomDtoToClassroom(classroomDto);
        classroom.setId(id);
        classroomService.update(classroom);

        HttpHeaders headers = new HttpHeaders();
        String location = builder.path("/classrooms/{id}")
                .buildAndExpand(classroom.getId()).toUriString();
        headers.add("location", location);

        return new ResponseEntity<>(classroom, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        classroomService.delete(id);
    }
}