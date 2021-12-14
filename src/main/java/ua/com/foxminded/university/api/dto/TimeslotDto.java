package ua.com.foxminded.university.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeslotDto {

    @NotNull
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime beginTime;
    @NotNull
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime endTime;
}
