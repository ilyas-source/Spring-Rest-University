package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import javax.sql.DataSource;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

class JdbcAddressDAOTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcAddressDAO jdbcAddressDAO;
    @Autowired
    private IDatabaseTester databaseTester;
    @Autowired
    private JdbcDatabaseTester jdbcDatabaseTester;

    public JdbcAddressDAOTest() {

    }

    @BeforeAll
    public static void createEmptyDb(DataSource dataSource) {
	Resource resource = new ClassPathResource("schema.sql");
	ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
	databasePopulator.execute(dataSource);
    }

    @BeforeEach
    void fillTables() throws Exception {
//	databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, databaseConnector.getConnection().getMetaData().getURL());
	databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
	databaseTester.setDataSet(readDataSet());
	databaseTester.onSetup();
    }

    @Test
    void addToDb_shouldAddress_() {
	fail("Not yet implemented");
    }

    @Test
    void testFindById() {
	fail("Not yet implemented");
    }

    @Test
    void testFindAll() {
	fail("Not yet implemented");
    }

    @Test
    void testUpdate() {
	fail("Not yet implemented");
    }

    @Test
    void testDelete() {
	fail("Not yet implemented");
    }

    private IDataSet readDataSet() throws Exception {
	ClassLoader classLoader = getClass().getClassLoader();
	String file = classLoader.getResource("testdata.xml").getFile();
	return new FlatXmlDataSetBuilder().build(new FileInputStream(file));
    }
}
