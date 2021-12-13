package ua.com.foxminded.university.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
public class TimeslotDto {

    @NotNull
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime beginTime;
    @NotNull
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime endTime;
}
