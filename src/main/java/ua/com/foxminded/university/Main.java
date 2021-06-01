package ua.com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.dao.SubjectDAO;
import ua.com.foxminded.university.model.Subject;

public class Main {

    public static void main(String[] args) {

	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

	JdbcUniversityPopulator jdbcUniversityPopulator = context.getBean(JdbcUniversityPopulator.class);
	jdbcUniversityPopulator.populate();

	SubjectDAO subjectDAO = context.getBean(SubjectDAO.class);

	for (Subject s : subjectDAO.findAll()) {
	    System.out.println(s);
	}

	Menu menu = context.getBean(Menu.class);
	menu.start(0);

	System.out.println("Success.");
	context.close();
    }

}
