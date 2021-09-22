package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.model.Location;
import ua.com.foxminded.university.service.LocationService;

@Controller
@RequestMapping("/locations")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("location") Location location) {
        logger.debug("Received location to create: {}", location);

     //   Location location = new Location(building, Integer.valueOf(floor), Integer.valueOf(number));

        locationService.create(location);

        return "redirect:/classrooms";
    }
}