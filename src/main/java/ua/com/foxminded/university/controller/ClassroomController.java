package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.service.ClassroomService;

@Controller
@RequestMapping("/classrooms")
public class ClassroomController {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomController.class);

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping
    public String findAll(Model model) {
        logger.debug("Retrieving all classrooms to controller");
        model.addAttribute("classrooms", classroomService.findAll());
        return "classroomsView";
    }

    @PostMapping
    public String create(@RequestParam("name") String name, @RequestParam("capacity") String capacity, Model model) {
        logger.debug("Received from form: name {}, capacity {}",name,capacity);

        Location location=new Location("Test building", 10, 100);
        Classroom classroom=new Classroom(location, name, Integer.valueOf(capacity));

        classroomService.create(classroom);

        model.addAttribute("classrooms", classroomService.findAll());

        return "ClassroomsView";
    }
}
