package ua.com.foxminded.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.university.service.TeacherService;

@Controller
@RequestMapping("/")
public class UniversityController {

    private final TeacherService teacherService;
    private final StudentService studentService;

    public UniversityController(TeacherService teacherService, StudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @GetMapping
    public String university() {
        return "universityView";
    }
}
