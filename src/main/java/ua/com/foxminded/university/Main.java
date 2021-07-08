package ua.com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) throws Exception {
	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

	JdbcUniversityPopulator jdbcUniversityPopulator = context.getBean(JdbcUniversityPopulator.class);
	jdbcUniversityPopulator.populate();

	Menu menu = context.getBean(Menu.class);
	menu.start(0);
	context.close();
    }
}
