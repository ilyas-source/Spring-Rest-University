package ua.com.foxminded.university.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ua.com.foxminded.university.api.dto.TeacherDto;
import ua.com.foxminded.university.api.mapper.TeacherMapper;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/teachers")
public class TeacherRestController {

    private final TeacherService teacherService;
    private final TeacherMapper mapper;

    public TeacherRestController(TeacherService teacherService, TeacherMapper mapper) {
        this.teacherService = teacherService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<Teacher> findAll() {
        return teacherService.findAll();
    }

    @GetMapping("/paged")
    Page<Teacher> findAll(Pageable pageable) {
        return teacherService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Teacher getTeacher(@PathVariable int id) {
        return teacherService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Teacher> save(@RequestBody @Valid TeacherDto teacherDto,
                                        UriComponentsBuilder builder) {
        Teacher teacher = mapper.teacherDtoToTeacher(teacherDto);
        teacherService.create(teacher);

        return created(builder.path("/teachers/{id}").build(teacher.getId())).build();
    }

    @PutMapping("/{id}")
    public Teacher update(@PathVariable int id, @RequestBody @Valid TeacherDto teacherDto) {
        Teacher teacher = mapper.teacherDtoToTeacher(teacherDto);
        teacher.setId(id);
        teacherService.update(teacher);

        return teacher;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        teacherService.delete(id);
    }
}
