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
	Address address = new Address(4, "test", "test", "test", "test", "test");
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
		.of(new Address(2, "Poland", "54321", "Central region", "Warsaw", "Urszuli Ledochowskiej 3"));

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
	expected.add(new Address(1, "UK", "12345", "City-Of-Edinburgh", "Edinburgh", "Panmure House"));
	expected.add(new Address(2, "Poland", "54321", "Central region", "Warsaw", "Urszuli Ledochowskiej 3"));
	expected.add(new Address(3, "Russia", "450080", "Permskiy kray", "Perm", "Lenina 5"));

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
	Address address = new Address(2, "test", "test", "test", "test", "test");

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