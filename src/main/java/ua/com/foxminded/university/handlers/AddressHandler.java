package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.scanner;

import ua.com.foxminded.university.model.Address;

public class AddressHandler {

    public static Address getAddressFromScanner() {
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
}
