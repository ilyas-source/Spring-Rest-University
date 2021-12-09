package ua.com.foxminded.university.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupRestController {

    private final GroupService groupService;

    public GroupRestController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<Group> findAll() {
        return groupService.findAll();
    }

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable int id) {
        return groupService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Group group) {
        groupService.create(group);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody Group group) {
        group.setId(id);
        groupService.update(group);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        groupService.delete(id);
    }
}