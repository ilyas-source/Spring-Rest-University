package ua.com.foxminded.university.menu;

import static java.util.Objects.isNull;
import static ua.com.foxminded.university.Menu.scanner;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.model.Gender;

@Component
public class GenderMenu {

    public Gender getGender() {
	Gender result = null;

	while (isNull(result)) {
	    System.out.print("Gender (M/F): ");
	    String choice = scanner.nextLine().toLowerCase();
	    switch (choice) {
	    case "m":
		result = Gender.MALE;
		break;
	    case "f":
		result = Gender.FEMALE;
		break;
	    default:
		System.out.println("Wrong input, try again.");
	    }
	}
	return result;
    }
}
