package ua.com.foxminded.university.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {

    @Size(min = 2, max = 12, message = "{length.between}")
    private String name;
}
