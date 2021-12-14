package ua.com.foxminded.university.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.foxminded.university.model.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherDto {

    @NotEmpty(message = "{name.notempty}")
    private String firstName;
    @NotEmpty(message = "{lastname.notempty}")
    private String lastName;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Degree degree;
    @NotEmpty
    private Set<SubjectDto> subjects;
    @Email
    private String email;
    private String phoneNumber;
    private AddressDto address;
    private List<VacationDto> vacations;
}
