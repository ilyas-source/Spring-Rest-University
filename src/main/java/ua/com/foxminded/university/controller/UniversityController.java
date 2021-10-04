package ua.com.foxminded.university.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String university(Model model) {
        model.addAttribute("teachers", teacherService.findAll(PageRequest.of(0, 1000)));
        model.addAttribute("students",studentService.findAll(PageRequest.of(0, 1000)));
        return "universityView";
    }
}
