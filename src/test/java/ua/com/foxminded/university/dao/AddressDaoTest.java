package ua.com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;
import ua.com.foxminded.university.model.Address;

@SpringJUnitConfig(SpringTestConfig.class)
@Sql(scripts = { "classpath:schema.sql", "classpath:fill-addresses.sql" })
class AddressDaoTest {

    private static final String TEST_WHERE_CLAUSE = "country='test' AND postalCode = 'test' AND region='test' AND city='test' AND streetAddress='test'";

    @Autowired
    private JdbcAddressDao addressDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenNewAddress_onCreate_shouldCreateAddress() {
	Address address = new Address.Builder("test").id(4).postalCode("test").region("test")
		.city("test").streetAddress("test").build();

	int rowsBeforeCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"addresses", "id = 4 AND " + TEST_WHERE_CLAUSE);

	addressDao.create(address);

	int rowsAfterCreate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
		"addresses", "id = 4 AND " + TEST_WHERE_CLAUSE);

	assertEquals(rowsAfterCreate, rowsBeforeCreate + 1);
    }

    @Test
    void givenCorrectAddressId_onFindById_shouldReturnOptionalWithCorrectAddress() {
	Optional<Address> expected = Optional
		.of(new Address.Builder("Poland").id(2).postalCode("54321").region("Central region").city("Warsaw")
			.streetAddress("Urszuli Ledochowskiej 3").build());

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
	List<Address> expected = new ArrayList<>();
	expected.add(new Address.Builder("UK").id(1).postalCode("12345").region("City-Of-Edinburgh").city("Edinburgh")
		.streetAddress("Panmure House").build());
	expected.add(new Address.Builder("Poland").id(2).postalCode("54321").region("Central region").city("Warsaw")
		.streetAddress("Urszuli Ledochowskiej 3").build());
	expected.add(new Address.Builder("Russia").id(3).postalCode("450080").region("Permskiy kray").city("Perm")
		.streetAddress("Lenina 5").build());

	List<Address> actual = addressDao.findAll();

	assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasNoAddresses_onFindAll_shouldReturnEmptyListOfAddresses() {
	JdbcTestUtils.deleteFromTables(jdbcTemplate, "addresses");

	List<Address> addresses = addressDao.findAll();

	assertThat(addresses).isEmpty();
    }

    @Test
    void givenAddress_onUpdate_shouldUpdateCorrectly() {
	Address address = new Address.Builder("test").id(2).postalCode("test").region("test")
		.city("test").streetAddress("test").build();

	addressDao.update(address);

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
}