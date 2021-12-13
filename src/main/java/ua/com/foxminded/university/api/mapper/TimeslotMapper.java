package ua.com.foxminded.university.api.mapper;

import org.mapstruct.Mapper;
import ua.com.foxminded.university.api.dto.TimeslotDto;
import ua.com.foxminded.university.model.Timeslot;

@Mapper(componentModel = "spring")
public interface TimeslotMapper {
    TimeslotDto timeslotToTimeslotDTO(Timeslot timeslot);

    Timeslot timeslotDtoToTimeslot(TimeslotDto timeslotDto);
}
