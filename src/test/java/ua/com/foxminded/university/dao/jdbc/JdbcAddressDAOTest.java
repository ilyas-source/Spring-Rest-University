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
}
