package ua.com.foxminded.university.menu;

import static ua.com.foxminded.university.Menu.scanner;

import ua.com.foxminded.university.model.Address;

public class AddressMenu {

    public Address createAddress() {
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
	return new Address(country, index, region, city, streetAddress);
    }

    public String getStringFromAddress(Address address) {
	return address.getPostalCode() + " " + address.getCountry() + ", "
		+ address.getRegion() + ", " + address.getCity() + ", " + address.getStreetAddress();
    }
}
