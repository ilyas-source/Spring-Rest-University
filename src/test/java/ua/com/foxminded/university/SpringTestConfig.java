package ua.com.foxminded.university;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;

@Configuration
@Import(ApplicationConfig.class)
public class SpringTestConfig {

    @Bean
    public DataSource dataSource() {
	return new EmbeddedDatabaseBuilder()
		.setType(EmbeddedDatabaseType.H2)
		.build();
    }
}