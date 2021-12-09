package ua.com.foxminded.university.api.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
public class LocationDto {

        @NotEmpty(message = "{name.notempty}")
        private String building;
        @Positive(message = "{floor.positive}")
        private int floor;
        @Positive(message = "{roomnumber.positive}")
        private int roomNumber;
}
