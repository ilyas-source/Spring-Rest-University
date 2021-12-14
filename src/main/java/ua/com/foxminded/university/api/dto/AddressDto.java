package ua.com.foxminded.university.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {

    private String country;
    private String postalCode;
    private String region;
    private String city;
    private String streetAddress;
}
