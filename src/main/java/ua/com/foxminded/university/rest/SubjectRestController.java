package ua.com.foxminded.university.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.service.SubjectService;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectRestController {

    private final SubjectService subjectService;

    public SubjectRestController(SubjectService subjectService) {
        this.subjectService = subjectService;
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
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Subject subject) {
        subjectService.create(subject);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody Subject subject) {
        subject.setId(id);
        subjectService.update(subject);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        subjectService.delete(id);
    }
}


