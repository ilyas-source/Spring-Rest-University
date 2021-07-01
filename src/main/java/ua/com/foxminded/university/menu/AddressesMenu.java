package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.scanner;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.model.Address;

@Component
public class AddressesMenu {

    public Address createAddress() {
	System.out.println("Entering address.");
	System.out.print("Country:");
	String country = scanner.nextLine();
	System.out.print("ZIP Code:");
	String index = scanner.nextLine();
	System.out.print("Region:");
	String region = scanner.nextLine();
	System.out.print("City:");
	String city = scanner.nextLine();
	System.out.print("Street Address:");
	String streetAddress = scanner.nextLine();

	return new Address.Builder(country)
		.postalCode(index)
		.region(region)
		.city(city)
		.streetAddress(streetAddress)
		.build();
    }

    public String getStringFromAddress(Address address) {
	return address.getPostalCode() + " " + address.getCountry() + ", "
		+ address.getRegion() + ", " + address.getCity() + ", " + address.getStreetAddress();
    }
}
