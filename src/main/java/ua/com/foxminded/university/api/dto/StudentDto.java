package ua.com.foxminded.university.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.validation.BirthDateConstraint;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    @NotEmpty(message = "{name.notempty}")
    private String firstName;
    @NotEmpty(message = "{lastname.notempty}")
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @BirthDateConstraint(age = 13)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;
    @Email(message = "{email.wrongformat}")
    private String email;
    private String phone;
    private AddressDto address;
    private GroupDto group;
}
