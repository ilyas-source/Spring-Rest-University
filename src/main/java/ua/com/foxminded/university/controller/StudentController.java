package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

//    public String findAll(Model model,
//                          @RequestParam("page") Optional<Integer> page,
//                          @RequestParam("size") Optional<Integer> size) {
//        logger.debug("Retrieving all students to controller");
//
//        int currentPage = page.orElse(1);
//        int pageSize = size.orElse(5);
//
//        Page<Student> studentPage = studentService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
//
//        model.addAttribute("studentPage", studentPage);
//
//        int totalPages = studentPage.getTotalPages();
//        if (totalPages > 0) {
//            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
//                    .boxed()
//                    .collect(Collectors.toList());
//            model.addAttribute("pageNumbers", pageNumbers);
//        }
//
//        model.addAttribute("students", studentService.findAll());
//        return "studentsView";
//    }

    @GetMapping
    public String findAll(Model model) {
        logger.debug("Retrieving all students to controller");
        model.addAttribute("students", studentService.findAll());
        return "studentsView";
    }
}

