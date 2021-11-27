package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;
import ua.com.foxminded.university.service.SubjectService;
import ua.com.foxminded.university.service.TeacherService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    private final TeacherService teacherService;
    private final SubjectService subjectService;


    public TeacherController(TeacherService teacherService, SubjectService subjectService) {
        this.teacherService = teacherService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public String getTeachers(Model model, Pageable pageable) {
        logger.debug("Retrieving page {}, size {}, sort {}", pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort());
        Page<Teacher> teacherPage = teacherService.findAll(pageable);
        model.addAttribute("teacherPage", teacherPage);
        return "teacher/all";
    }

    @GetMapping("/{id}")
    public String getTeacher(@PathVariable int id, Model model) {
        Teacher teacher = teacherService.getById(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("allSubjects", subjectService.findAll());
        return "teacher/details";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model, Teacher teacher) {
        logger.debug("Opening creation form");
        model.addAttribute("allSubjects", subjectService.findAll());
        return "teacher/create";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("teacher") @Valid Teacher teacher, BindingResult result) {
        logger.debug("Received update data: {}", teacher);
        if(result.hasErrors()) {
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        refreshFieldsFromDatabase(teacher);
        teacherService.update(teacher);
        return "redirect:/teachers";
    }

    private void refreshFieldsFromDatabase(Teacher teacher) {
        List<Vacation> existingVacations = teacherService.getById(teacher.getId()).getVacations();
        teacher.setVacations(existingVacations);

        Set<Subject> subjects = teacher.getSubjects();
        for (Subject s : subjects) {
            logger.debug("Filling out subject: {}", s);
            Subject retrievedSubject = subjectService.getById(s.getId());
            s.setName(retrievedSubject.getName());
            s.setDescription(retrievedSubject.getDescription());
            logger.debug("Filled: {}", s);
        }
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("teacher") @Valid Teacher teacher, BindingResult result) {
        logger.debug("Create teacher={}", teacher);
        if(result.hasErrors()) {
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        teacherService.create(teacher);
        return "redirect:/teachers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        teacherService.delete(id);
        return "redirect:/teachers";
    }
}
