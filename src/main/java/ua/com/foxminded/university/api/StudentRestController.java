package ua.com.foxminded.university.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ua.com.foxminded.university.api.dto.StudentDto;
import ua.com.foxminded.university.api.mapper.StudentMapper;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.StudentService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {

    private final StudentService studentService;
    private final StudentMapper mapper;

    public StudentRestController(StudentService studentService, StudentMapper mapper) {
        this.studentService = studentService;
        this.mapper = mapper;
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
    public ResponseEntity<Student> save(@RequestBody @Valid StudentDto studentDto,
                                        UriComponentsBuilder builder) {
        Student student = mapper.studentDtoToStudent(studentDto);
        studentService.create(student);

        return created(builder.path("/students/{id}").build(student.getId())).build();
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable int id, @RequestBody @Valid StudentDto studentDto) {
        Student student = mapper.studentDtoToStudent(studentDto);
        student.setId(id);
        studentService.update(student);

        return student;
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        studentService.delete(id);
    }
}