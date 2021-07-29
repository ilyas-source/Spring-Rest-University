package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	model.addAttribute("groups", groupService.findAll());
	return "groups";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable int id, Model model) {
	model.addAttribute("group", groupService.findById(id).get());
	return "info/group";
    }

    @PostMapping("/add")
    public String addGroup(@ModelAttribute Group group) {
	groupService.create(group);
	return "redirect:/university/groups";
    }

    @PostMapping("/edit")
    public String updateGroup(@ModelAttribute Group group) {
	groupService.update(group);
	return "redirect:/university/groups";
    }

//    @PostMapping("/delete")
//    public String deleteGroup(@ModelAttribute Group group) {
//        groupService.delete(group);
//        return "redirect:/university/groups";
//    }
}
