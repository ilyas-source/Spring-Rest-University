package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public String getGroup(@PathVariable int id, Model model) {
        Group group = groupService.getById(id);
        model.addAttribute("group", group);
        return "/details/group";
    }

    @GetMapping("/new")
    public String showCreationForm(Model model) {
        logger.debug("Opening creation form");
        model.addAttribute("group", new Group());
        return "/create/group";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("group") Group group) {
        logger.debug("Create group={}", group);
        groupService.create(group);
        return "redirect:/groups";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("group") Group group) {
        logger.debug("Update group={}", group);
        groupService.update(group);
        return "redirect:/groups";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        groupService.delete(id);
        return "redirect:/groups";
    }
}
