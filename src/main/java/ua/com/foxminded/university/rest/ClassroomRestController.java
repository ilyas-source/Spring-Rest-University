package ua.com.foxminded.university.rest;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.dto.ClassroomDto;
import ua.com.foxminded.university.rest.assembler.ClassroomModelAssembler;
import ua.com.foxminded.university.service.ClassroomService;
import ua.com.foxminded.university.service.LocationService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomRestController {

    private final ClassroomService classroomService;
    private final LocationService locationService;
    private final ClassroomModelAssembler assembler;

    public ClassroomRestController(ClassroomService classroomService, LocationService locationService,
                                   ClassroomModelAssembler assembler) {
        this.classroomService = classroomService;
        this.locationService = locationService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Classroom>> findAll() {
        System.out.println(classroomService.findAll());
        List<EntityModel<Classroom>> employees = classroomService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(ClassroomRestController.class).findAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Classroom> getClassroom(@PathVariable int id) {
        Classroom classroom = classroomService.getById(id);

        return assembler.toModel(classroom);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ClassroomDto classroomDto) {
        Classroom classroom=convertDtoToClassroom(classroomDto);
        EntityModel<Classroom> entityModel = assembler.toModel(classroomService.create(classroom));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    private Classroom convertDtoToClassroom(ClassroomDto classroomDto) {
        return new Classroom(locationService.getById(classroomDto.getLocationId()),
                classroomDto.getName(), classroomDto.getCapacity());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody ClassroomDto classroomDto) {
        Classroom classroom=convertDtoToClassroom(classroomDto);
        classroom.setId(id);
        EntityModel<Classroom> entityModel = assembler.toModel(classroomService.update(classroom));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        classroomService.delete(id);

        return ResponseEntity.noContent().build();
    }
}