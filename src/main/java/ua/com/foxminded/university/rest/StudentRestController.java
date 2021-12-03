package ua.com.foxminded.university.rest;

import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {

    private final StudentService studentService;

    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> findAll() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable int id) {
        return studentService.getById(id);
    }

    @PostMapping
    public void save(@RequestBody Student student) {
        student.setId(0);
        studentService.create(student);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody Student student) {
        student.setId(id);
        studentService.update(student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        studentService.delete(id);
    }
}