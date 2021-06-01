package ua.com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.dao.SubjectDAO;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.University;

public class Main {

    public static void main(String[] args) {

	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
	SubjectDAO subjectDAO = context.getBean(SubjectDAO.class);

	for (Subject s : subjectDAO.findAll()) {
	    System.out.println(s);
	}

	University university = new University();
	UniversityPopulator universityPopulator = new UniversityPopulator(university);
	universityPopulator.populate();

	Menu menu = new Menu(university);
	menu.start(0);

	context.close();
    }

}
