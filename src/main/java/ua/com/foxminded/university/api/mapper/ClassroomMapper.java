package ua.com.foxminded.university.api.mapper;

import org.mapstruct.Mapper;
import ua.com.foxminded.university.api.dto.ClassroomDto;
import ua.com.foxminded.university.api.dto.LocationDto;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;

@Mapper(componentModel = "spring")
public interface ClassroomMapper {
    ClassroomDto classroomToClassroomDTO(Classroom classroom);

    Classroom classroomDtoToClassroom(ClassroomDto classroomDto);

    LocationDto locationToLocationDto(Location Location);

    Location locationDtoToLocation(LocationDto locationDto);
}
