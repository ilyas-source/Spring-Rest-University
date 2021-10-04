package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;
import ua.com.foxminded.university.service.SubjectService;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.service.VacationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    private final TeacherService teacherService;
    private final SubjectService subjectService;
    private final VacationService vacationService;

    public TeacherController(TeacherService teacherService, SubjectService subjectService, VacationService vacationService) {
        this.teacherService = teacherService;
        this.subjectService = subjectService;
        this.vacationService = vacationService;
    }

    @GetMapping
    public String getTeachers(Model model, Pageable pageable) {
        logger.debug("Retrieving page {}, size {}, sort {}", pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort());
        Page<Teacher> teacherPage = teacherService.findAll(pageable);
        model.addAttribute("teacherPage", teacherPage);
        return "teachersView";
    }

    @GetMapping("/{id}")
    public String getTeacher(@PathVariable int id, Model model) {
        Teacher teacher = teacherService.getById(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("allSubjects", subjectService.findAll());
        return "/details/teacher";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        logger.debug("Opening creation form");
        model.addAttribute("teacher", new Teacher());
        return "/create/teacher";
    }

    @GetMapping("/newvacation")
    public String showNewVacationForm(Model model) {
        logger.debug("Opening new vacation form");
        model.addAttribute("teacher", new Teacher());
        return "/create/vacation";
    }

    @PostMapping("/addVacation")
    public String addVacation(@RequestParam("start") String start,
                              @RequestParam("end") String end,
                              @RequestParam("id") String teacherId, Model model) {
        logger.debug("Received vacation start {}, end{} for teacher id:{}", start, end, teacherId);
        int id=Integer.valueOf(teacherId);
        LocalDate startDate=LocalDate.parse(start,dateTimeFormatter);
        LocalDate endDate=LocalDate.parse(end,dateTimeFormatter);

        var vacation= new Vacation(startDate,endDate);
        logger.debug("Created vacation: {}", vacation);

        Teacher teacher = teacherService.getById(id);

        vacationService.create(vacation);

        teacher.getVacations().add(vacation);

        teacherService.update(teacher);

        return "redirect:/teachers/editvacations/"+teacherId;
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("teacher") Teacher teacher) {
        logger.debug("Received update data: {}", teacher);
        teacherService.update(teacher);
        return "redirect:/teachers";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("teacher") Teacher teacher) {
        logger.debug("Received to create: {}", teacher);
        teacherService.create(teacher);
        return "redirect:/teachers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        teacherService.delete(id);
        return "redirect:/teachers";
    }

    @GetMapping("/editvacations/{id}")
    public String editVacations(@PathVariable int id, Model model) {
        logger.debug("Begin editing vacations for teacher id:{}", id);
        Teacher teacher = teacherService.getById(id);
        model.addAttribute(teacher);

        return "teachervacationsView";
    }
}
