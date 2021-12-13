package ua.com.foxminded.university.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomDto {

    @NotEmpty(message = "{name.notempty}")
    private String name;
    @Positive(message = "{capacity.positive}")
    private int capacity;
    @Valid
    private LocationDto location;
}
