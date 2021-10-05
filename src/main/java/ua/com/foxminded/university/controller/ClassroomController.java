package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.service.ClassroomService;
import ua.com.foxminded.university.service.LocationService;

@Controller
@RequestMapping("/classrooms")
public class ClassroomController {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomController.class);

    private final ClassroomService classroomService;
    private final LocationService locationService;

    public ClassroomController(ClassroomService classroomService, LocationService locationService) {
        this.classroomService = classroomService;
        this.locationService = locationService;
    }

    @GetMapping
    public String findAll(Model model) {
        logger.debug("Retrieving all classrooms to controller");
        model.addAttribute("classrooms", classroomService.findAll());
        return "classroomsView";
    }

    @GetMapping("/{id}")
    public String getClassroom(@PathVariable int id, Model model) {
        Classroom classroom = classroomService.getById(id);
        model.addAttribute("classroom", classroom);
        model.addAttribute("locations", locationService.findAll());
        model.addAttribute("location", new Location());
        return "/details/classroom";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        logger.debug("Opening creation form");
        model.addAttribute("classroom", new Classroom());
        model.addAttribute("location", new Location());
        model.addAttribute("locations", locationService.findAll());
        return "/create/classroom";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("classroom") Classroom classroom) {
        logger.debug("Received classroom to create: {}", classroom);
        locationService.create(classroom.getLocation());
        classroomService.create(classroom);
        return "redirect:/classrooms";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("classroom") Classroom classroom) {
        logger.debug("Received classroom to update: {}", classroom);
        locationService.update(classroom.getLocation());
        classroomService.update(classroom);
        return "redirect:/classrooms";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        classroomService.delete(id);
        return "redirect:/classrooms";
    }
}
