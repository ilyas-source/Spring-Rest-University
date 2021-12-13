package ua.com.foxminded.university.api.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class GroupDto {

    @Size(min = 2, max = 12, message = "{length.between}")
    private String name;
}
