package ua.com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

	JdbcUniversityPopulator jdbcUniversityPopulator = context.getBean(JdbcUniversityPopulator.class);
	jdbcUniversityPopulator.populate();

	Menu menu = context.getBean(Menu.class);
	menu.start(6);
	context.close();
    }
}
