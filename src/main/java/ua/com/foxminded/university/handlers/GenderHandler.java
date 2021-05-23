package ua.com.foxminded.university.handlers;

import static ua.com.foxminded.university.Menu.*;

import ua.com.foxminded.university.model.Gender;

public class GenderHandler {

    public static Gender getGenderFromScanner() {
	Boolean keepOn = true;
	String choice = "";
	while (keepOn) {
	    choice = scanner.nextLine().toLowerCase();
	    if (choice.equals("m") || choice.equals("f")) {
		keepOn = false;
	    } else {
		System.out.print("Wrong input, try again: ");
	    }
	}
	if (choice == "m") {
	    return Gender.MALE;
	} else {
	    return Gender.FEMALE;
	}
    }
}
