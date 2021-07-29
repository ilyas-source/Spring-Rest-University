package ua.com.foxminded.university;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import ua.com.foxminded.university.config.ApplicationConfig;

@Configuration
@Import(ApplicationConfig.class)
public class SpringTestConfig {

    @Bean
    public DataSource dataSource() {
	return new EmbeddedDatabaseBuilder()
		.setType(EmbeddedDatabaseType.H2)
		.addScript("schema.sql")
		.addScript("test-data.sql")
		.build();
    }
}