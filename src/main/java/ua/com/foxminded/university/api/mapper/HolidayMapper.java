package ua.com.foxminded.university.api.mapper;

import org.mapstruct.Mapper;
import ua.com.foxminded.university.api.dto.HolidayDto;
import ua.com.foxminded.university.model.Holiday;

@Mapper(componentModel = "spring")
public interface HolidayMapper {
    HolidayDto holidayToHolidayDTO(Holiday holiday);

    Holiday holidayDtoToHoliday(HolidayDto holidayDto);
}