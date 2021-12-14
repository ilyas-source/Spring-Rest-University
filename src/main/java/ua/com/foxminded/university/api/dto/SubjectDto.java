package ua.com.foxminded.university.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDto {

    @NotEmpty(message = "{name.notempty}")
    private String name;
    private String description;
  //  private List<TeacherDto> teachers;
}
