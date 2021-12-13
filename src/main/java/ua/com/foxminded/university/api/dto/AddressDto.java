package ua.com.foxminded.university.api.dto;

import lombok.Data;

@Data
public class AddressDto {

    private String country;
    private String postalCode;
    private String region;
    private String city;
    private String streetAddress;
}
