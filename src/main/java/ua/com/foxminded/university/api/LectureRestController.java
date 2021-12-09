package ua.com.foxminded.university.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.LectureService;
import ua.com.foxminded.university.service.TeacherService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureRestController {

    private final LectureService lectureService;
    private final TeacherService teacherService;

    public LectureRestController(LectureService lectureService, TeacherService teacherService) {
        this.lectureService = lectureService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<Lecture> findAll() {
        return lectureService.findAll();
    }

    @GetMapping("/{id}")
    public Lecture getLecture(@PathVariable int id) {
        return lectureService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Lecture lecture) {
        lectureService.create(lecture);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody Lecture lecture) {
        lecture.setId(id);
        lectureService.update(lecture);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        lectureService.delete(id);
    }

    @PostMapping("/replacement")
    public void replaceTeacher(@RequestParam("teacher") int id,
                                 @RequestParam("start") LocalDate start,
                                 @RequestParam("end") LocalDate end) {

        Teacher teacher = teacherService.getById(id);
        lectureService.replaceTeacher(teacher, start, end);
    }
}