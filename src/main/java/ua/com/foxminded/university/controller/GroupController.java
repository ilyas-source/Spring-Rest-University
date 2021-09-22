package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public String findAll(Model model) {
        logger.debug("Retrieving all groups to controller");
        model.addAttribute("groups", groupService.findAll());

        return "groupsView";
    }

    @GetMapping("/{id}")
    public String showDetails(@PathVariable int id, Model model) {
        Group group = groupService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find group by id " + id));
        model.addAttribute("group", group);

        return "/details/group";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("group") Group group) {
        logger.debug("Received update data: name {}", group.getName());
        groupService.update(group);

        return "redirect:/groups";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        logger.debug("Opening creation form");
        model.addAttribute("group", new Group());

        return "/create/group";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("group") Group group) {
        logger.debug("Received to create: {}", group);
        groupService.create(group);

        return "redirect:/groups";
    }
}
