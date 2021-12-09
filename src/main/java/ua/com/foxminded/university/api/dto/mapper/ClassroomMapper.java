package ua.com.foxminded.university.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ua.com.foxminded.university.api.dto.ClassroomDto;
import ua.com.foxminded.university.api.dto.LocationDto;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;

@Mapper
public interface ClassroomMapper {
    ClassroomMapper INSTANCE = Mappers.getMapper(ClassroomMapper.class);

    ClassroomDto classroomToClassroomDTO(Classroom classroom);
    @Mapping(target="location", source = "locationDto")
    Classroom classroomDtoToClassroom(ClassroomDto classroomDto);

    LocationDto locationToLocationDto(Location Location);
    Location locationDtoToLocation(LocationDto locationDto);
}
