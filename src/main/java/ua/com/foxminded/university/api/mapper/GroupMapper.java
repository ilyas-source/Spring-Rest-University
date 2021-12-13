package ua.com.foxminded.university.api.mapper;

import org.mapstruct.Mapper;
import ua.com.foxminded.university.api.dto.GroupDto;
import ua.com.foxminded.university.model.Group;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupDto groupToGroupDTO(Group group);

    Group groupDtoToGroup(GroupDto groupDto);
}


