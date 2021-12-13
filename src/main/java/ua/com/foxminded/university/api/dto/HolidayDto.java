package ua.com.foxminded.university.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class HolidayDto {

    @NotNull
    private LocalDate date;
    @NotEmpty(message = "{name.notempty}")
    private String name;
}
