package ua.com.foxminded.university.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ua.com.foxminded.university.api.dto.LectureDto;
import ua.com.foxminded.university.api.mapper.LectureMapper;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.LectureService;
import ua.com.foxminded.university.service.TeacherService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/lectures")
public class LectureRestController {

    private final LectureService lectureService;
    private final TeacherService teacherService;
    private final LectureMapper mapper;

    public LectureRestController(LectureService lectureService, TeacherService teacherService, LectureMapper mapper) {
        this.lectureService = lectureService;
        this.teacherService = teacherService;
        this.mapper = mapper;
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
    public ResponseEntity<Lecture> save(@RequestBody @Valid LectureDto lectureDto,
                                        UriComponentsBuilder builder) {
        Lecture lecture = mapper.lectureDtoToLecture(lectureDto);
        lectureService.create(lecture);

        return created(builder.path("/lectures/{id}").build(lecture.getId())).build();
    }

    @PutMapping("/{id}")
    public Lecture update(@PathVariable int id, @RequestBody @Valid LectureDto lectureDto) {
        Lecture lecture = mapper.lectureDtoToLecture(lectureDto);
        lecture.setId(id);
        lectureService.update(lecture);

        return lecture;
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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