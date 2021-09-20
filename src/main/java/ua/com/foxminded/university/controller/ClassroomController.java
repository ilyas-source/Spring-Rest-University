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

    @GetMapping("/creationform")
    public String creationForm(Model model) {
        logger.debug("Opening creation form");
        model.addAttribute("locations", locationService.findAll());
        return "/create/classroom";
    }

    @PostMapping("/create")
    public String create(@RequestParam("name") String name,
                         @RequestParam("capacity") String capacity,
                         @RequestParam("locationid") String locationid, Model model) {
        logger.debug("Received to create: name {}, capacity {}, location id={}", name, capacity, locationid);

        Location location = locationService.findById(Integer.parseInt(locationid)).get();
        Classroom classroom = new Classroom(location, name, Integer.valueOf(capacity));

        classroomService.create(classroom);

        model.addAttribute("classrooms", classroomService.findAll());

        return "classroomsView";
    }

    @PostMapping("/update")
    public String update(@RequestParam("name") String name,
                         @RequestParam("capacity") String capacity,
                         @RequestParam("locationid") String locationid,
                         @RequestParam("id") String id, Model model) {
        logger.debug("Received update data: name {}, capacity {}, location id={}", name, capacity, locationid);

        Location location = locationService.findById(Integer.parseInt(locationid)).get();

        Classroom classroom = new Classroom(Integer.valueOf(id),location,name,Integer.valueOf(capacity));

        classroomService.update(classroom);

        model.addAttribute(classroom);

        return "/details/classroom";
    }

    @GetMapping("/delete/{id}")
    public String deleteClassroom(@PathVariable int id, Model model) {
        classroomService.delete(id);

        model.addAttribute("classrooms", classroomService.findAll());

        return "classroomsView";
    }

    @GetMapping("/{id}")
    public String classroomDetails(@PathVariable int id, Model model) {
        model.addAttribute("classroom", classroomService.findById(id).get());
        model.addAttribute("locations", locationService.findAll());

        return "/details/classroom";
    }
}
