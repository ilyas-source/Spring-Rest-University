package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.service.LocationService;

@Controller
@RequestMapping("/locations")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

//    @PostMapping("/create")
//    public String create(@ModelAttribute("location") Location location) {
//        logger.debug("Received location to create: {}", location);
//        locationService.create(location);
//        return "redirect:/classrooms";
//    }
//
//    @PostMapping("/delete/{id}")
//    public String delete(@PathVariable int id) {
//        locationService.delete(id);
//        return "redirect:/classrooms";
//    }
}