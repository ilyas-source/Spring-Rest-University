package ua.com.foxminded.university.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureDto {

    @NotNull
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;
    @NotNull(message = "{assign.timeslot}")
    private TimeslotDto timeslot;
    @NotEmpty(message = "{assign.group}")
    private Set<GroupDto> groups;
    @NotNull(message = "{assign.subject}")
    private SubjectDto subject;
    @NotNull(message = "{assign.teacher}")
    private TeacherDto teacher;
    @NotNull(message = "{assign.classroom}")
    private ClassroomDto classroom;
}
