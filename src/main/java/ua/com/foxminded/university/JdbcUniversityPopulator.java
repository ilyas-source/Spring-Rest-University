package ua.com.foxminded.university;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

@Component
public class JdbcUniversityPopulator {

    private DataSource dataSource;

    public JdbcUniversityPopulator(DataSource dataSource) {
	this.dataSource = dataSource;
    }

    public void populate() {
	executeSqlScriptFromFile(dataSource, "schema.sql");
	executeSqlScriptFromFile(dataSource, "data.sql");
    }

    private void executeSqlScriptFromFile(DataSource dataSource, String fileName) {
	Resource resource = new ClassPathResource(fileName);
	ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
	databasePopulator.execute(dataSource);
    }
}
