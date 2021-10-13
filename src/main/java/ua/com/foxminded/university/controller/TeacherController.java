package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;
import ua.com.foxminded.university.service.SubjectService;
import ua.com.foxminded.university.service.TeacherService;

import java.util.List;

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
    public String update(@ModelAttribute("teacher") Teacher teacher) {
        logger.debug("Received update data: {}", teacher);
        refreshFieldsFromDatabase(teacher);
        teacherService.update(teacher);
        return "redirect:/teachers";
    }

    private void refreshFieldsFromDatabase(Teacher teacher) {
        List<Vacation> existingVacations=teacherService.getById(teacher.getId()).getVacations();
        teacher.setVacations(existingVacations);

        List<Subject> subjects=teacher.getSubjects();
        for (Subject s: subjects) {
            logger.debug("Filling out subject: {}", s);
            Subject retrievedSubject=subjectService.getById(s.getId());
            s.setName(retrievedSubject.getName());
            s.setDescription(retrievedSubject.getDescription());
            logger.debug("Filled: {}", s);
        }
    }


//    private List<Subject> subjects;

//    private List<Vacation> vacations;

//    private void refreshFieldsFromDatabase(@ModelAttribute("lecture") Lecture lecture) {

//        List<Group> groups = lecture.getGroups();
//        for (Group group : groups) {
//            logger.debug("Received group {}, with id {}, name {}", group, group.getId(), group.getName());
//            group.setName(groupService.getById(group.getId()).getName());
//        }
//        logger.debug("Full lecture: {}", lecture);
//    }

    @PostMapping("/create")
    public String create(@ModelAttribute("teacher") Teacher teacher) {
        logger.debug("Create teacher={}", teacher);
        teacherService.create(teacher);
        return "redirect:/teachers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        teacherService.delete(id);
        return "redirect:/teachers";
    }
}
