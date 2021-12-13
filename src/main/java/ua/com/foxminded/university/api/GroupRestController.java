package ua.com.foxminded.university.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ua.com.foxminded.university.api.dto.GroupDto;
import ua.com.foxminded.university.api.mapper.GroupMapper;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/groups")
public class GroupRestController {

    private final GroupService groupService;
    private final GroupMapper mapper;

    public GroupRestController(GroupService groupService, GroupMapper mapper) {
        this.groupService = groupService;
        this.mapper = mapper;
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
    public ResponseEntity<Group> save(@RequestBody @Valid GroupDto groupDto,
                                      UriComponentsBuilder builder) {
        Group group = mapper.groupDtoToGroup(groupDto);
        groupService.create(group);

        return created(builder.path("/groups/{id}").build(group.getId())).build();
    }

    @PutMapping("/{id}")
    public Group update(@PathVariable int id, @RequestBody @Valid GroupDto groupDto) {
        Group group = mapper.groupDtoToGroup(groupDto);
        group.setId(id);
        groupService.update(group);

        return group;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        groupService.delete(id);
    }
}