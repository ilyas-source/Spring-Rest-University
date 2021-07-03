package ua.com.foxminded.university;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@Import(ApplicationConfig.class)
public class SpringTestConfig {

    @Bean
    public DataSource dataSource() {
	return new EmbeddedDatabaseBuilder()
		.setType(EmbeddedDatabaseType.H2)
		.build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(final DataSource dataSource) {
	return new DataSourceTransactionManager(dataSource);
    }
}