package ua.com.foxminded.university.rest;

import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.service.ClassroomService;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomRestController {

    private final ClassroomService classroomService;

    public ClassroomRestController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping
    public List<Classroom> findAll() {
        return classroomService.findAll();
    }

    @GetMapping("/{id}")
    public Classroom getClassroom(@PathVariable int id) {
        return classroomService.getById(id);
    }

    @PostMapping
    public void save(@RequestBody Classroom classroom) {
        classroom.setId(0);
        classroomService.create(classroom);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody Classroom classroom) {
        classroom.setId(id);
        classroomService.update(classroom);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        classroomService.delete(id);
    }
}