package ua.com.foxminded.university.api.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class SubjectDto {

    @NotEmpty(message = "{name.notempty}")
    private String name;
    private String description;
    private List<TeacherDto> teachers;
}
