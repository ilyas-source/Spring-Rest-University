package ua.com.foxminded.university.menu;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Address;

@Component
public class AddressesMenu {

    public String getStringFromAddress(Address address) {
        return address.getPostalCode() + " " + address.getCountry() + ", "
                + address.getRegion() + ", " + address.getCity() + ", " + address.getStreetAddress();
    }
}
