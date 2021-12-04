package ua.com.foxminded.university.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherRestController {

    private final TeacherService teacherService;

    public TeacherRestController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<Teacher> findAll() {
        return teacherService.findAll();
    }

    @GetMapping("/{id}")
    public Teacher getTeacher(@PathVariable int id) {
        return teacherService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Teacher teacher) {
        teacherService.create(teacher);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody Teacher teacher) {
        teacher.setId(id);
        teacherService.update(teacher);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        teacherService.delete(id);
    }
}
