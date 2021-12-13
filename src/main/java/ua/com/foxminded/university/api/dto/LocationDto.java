package ua.com.foxminded.university.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

        @NotEmpty(message = "{name.notempty}")
        private String building;
        @Positive(message = "{floor.positive}")
        private int floor;
        @Positive(message = "{roomnumber.positive}")
        private int roomNumber;
}
