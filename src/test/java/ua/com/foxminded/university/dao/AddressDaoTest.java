package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.model.Address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.com.foxminded.university.dao.AddressDaoTest.TestData.*;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AddressDaoTest {

    private static final String TEST_WHERE_CLAUSE = "country='test' AND postalCode = 'test' AND region='test' AND city='test' AND streetAddress='test'";

    @Autowired
    private AddressDao addressDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenNewAddress_onCreate_shouldCreateAddress() {
	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"addresses", "id=7 AND " + TEST_WHERE_CLAUSE);

	addressDao.create(addressToCreate);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"addresses", "id=7 AND " + TEST_WHERE_CLAUSE);

	assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

    @Test
    void givenCorrectAddressId_onFindById_shouldReturnOptionalWithCorrectAddress() {
	Optional<Address> expected = Optional.of(expectedAddress2);

	Optional<Address> actual = addressDao.findById(2);

	assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectAddressId_onFindById_shouldReturnEmptyOptional() {
	Optional<Address> expected = Optional.empty();

	Optional<Address> actual = addressDao.findById(10);

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasAddresses_onFindAll_shouldReturnCorrectListOfAddresses() {
	List<Address> actual = addressDao.findAll();

	assertEquals(expectedAddresses, actual);
    }

    @Test
    void ifDatabaseHasNoAddresses_onFindAll_shouldReturnEmptyListOfAddresses() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "addresses");

	List<Address> addresses = addressDao.findAll();

	assertThat(addresses).isEmpty();
    }

    @Test
    void givenAddress_onUpdate_shouldUpdateCorrectly() {
	addressDao.update(addressToUpdate);

	int rowsAfterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"addresses", "id = 2 AND " + TEST_WHERE_CLAUSE);

	assertThat(rowsAfterUpdate).isEqualTo(1);
    }

    @Test
    void givenCorrectAddressId_onDelete_shouldDeleteCorrectly() {
	int rowsBeforeDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "addresses", "id = 2");

	addressDao.delete(2);

	int rowsAfterDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "addresses", "id = 2");

	assertEquals(rowsAfterDelete, rowsBeforeDelete - 1);
    }

    public interface TestData {
	Address addressToUpdate = Address.builder().country("test").id(2).postalCode("test").region("test")
		.city("test").streetAddress("test").build();

	Address addressToCreate = Address.builder().country("test").id(7).postalCode("test").region("test").city("test")
		.streetAddress("test").build();

	Address expectedAddress1 = Address.builder().country("UK").id(1).postalCode("12345").region("City-Of-Edinburgh")
		.city("Edinburgh").streetAddress("Panmure House").build();
	Address expectedAddress2 = Address.builder().country("Poland").id(2).postalCode("54321").region("Central region")
		.city("Warsaw").streetAddress("Urszuli Ledochowskiej 3").build();
	Address expectedAddress3 = Address.builder().country("Russia").id(3).postalCode("450080").region("Permskiy kray")
		.city("Perm").streetAddress("Lenina 5").build();
	Address expectedAddress4 = Address.builder().country("USA").id(4).postalCode("90210").region("California")
		.city("LA").streetAddress("Grove St. 15").build();
	Address expectedAddress5 = Address.builder().country("France").id(5).postalCode("21012").region("Central")
		.city("Paris").streetAddress("Rue 15").build();
	Address expectedAddress6 = Address.builder().country("China").id(6).postalCode("20121").region("Guangdung")
		.city("Beijin").streetAddress("Main St. 125").build();

	List<Address> expectedAddresses = new ArrayList<>(Arrays.asList(expectedAddress1, expectedAddress2, expectedAddress3,
		expectedAddress4, expectedAddress5, expectedAddress6));
    }
}