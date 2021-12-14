package ua.com.foxminded.university.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayDto {

    @NotNull
    private LocalDate date;
    @NotEmpty(message = "{name.notempty}")
    private String name;
}
