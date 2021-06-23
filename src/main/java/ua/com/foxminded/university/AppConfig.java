package ua.com.foxminded.university;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan("ua.com.foxminded.university")
@PropertySource("classpath:database.properties")
public class AppConfig {

    @Value("${driver}")
    private String driver;
    @Value("${url}")
    private String url;
    @Value("${user}")
    private String user;
    @Value("${password}")
    private String password;

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
	return new JdbcTemplate(dataSource);
    }

    @Bean
    DataSource dataSource() {
	DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	driverManagerDataSource.setUrl(url);
	driverManagerDataSource.setUsername(user);
	driverManagerDataSource.setPassword(password);
	driverManagerDataSource.setDriverClassName(driver);
	return driverManagerDataSource;
    }
}