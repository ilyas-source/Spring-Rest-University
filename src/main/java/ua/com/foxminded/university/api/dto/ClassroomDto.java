package ua.com.foxminded.university.api.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
public class ClassroomDto {
    @NotEmpty(message = "{name.notempty}")
    private String name;
    @Positive(message = "{capacity.positive}")
    private int capacity;
    @Valid
    private LocationDto locationDto;
}
