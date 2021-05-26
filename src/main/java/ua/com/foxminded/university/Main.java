package ua.com.foxminded.university;

import ua.com.foxminded.university.model.University;

public class Main {

    public static void main(String[] args) {

	University university = new University();

	UniversityPopulator universityPopulator = new UniversityPopulator(university);
	universityPopulator.populate();

	Menu menu = new Menu(university);
	menu.start(0);
    }

}
