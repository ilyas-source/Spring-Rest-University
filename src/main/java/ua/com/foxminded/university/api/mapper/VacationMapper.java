package ua.com.foxminded.university.api.mapper;

import org.mapstruct.Mapper;
import ua.com.foxminded.university.api.dto.VacationDto;
import ua.com.foxminded.university.model.Vacation;

@Mapper(componentModel = "spring")
public interface VacationMapper {
    VacationDto vacationToVacationDTO(Vacation vacation);

    Vacation vacationDtoToVacation(VacationDto vacationDto);
}
