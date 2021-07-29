package ua.com.foxminded.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/university")
public class UniversityController {

    @GetMapping
    public String university() {
	return "university";
    }
}
