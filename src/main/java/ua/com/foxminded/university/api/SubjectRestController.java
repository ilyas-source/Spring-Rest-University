package ua.com.foxminded.university.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ua.com.foxminded.university.api.dto.SubjectDto;
import ua.com.foxminded.university.api.mapper.SubjectMapper;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.service.SubjectService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/subjects")
public class SubjectRestController {

    private final SubjectService subjectService;
    private final SubjectMapper mapper;

    public SubjectRestController(SubjectService subjectService, SubjectMapper mapper) {
        this.subjectService = subjectService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<Subject> findAll() {
        return subjectService.findAll();
    }

    @GetMapping("/{id}")
    public Subject getSubject(@PathVariable int id) {
        return subjectService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Subject> save(@RequestBody @Valid SubjectDto subjectDto,
                                        UriComponentsBuilder builder) {
        Subject subject = mapper.subjectDtoToSubject(subjectDto);
        subjectService.create(subject);

        return created(builder.path("/subjects/{id}").build(subject.getId())).build();
    }

    @PutMapping("/{id}")
    public Subject update(@PathVariable int id, @RequestBody @Valid SubjectDto subjectDto) {
        Subject subject = mapper.subjectDtoToSubject(subjectDto);
        subject.setId(id);
        subjectService.update(subject);

        return subject;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        subjectService.delete(id);
    }
}


