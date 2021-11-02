package ua.com.foxminded.university.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.SpringTestConfig;
import ua.com.foxminded.university.dao.hibernate.HibernateAddressDao;
import ua.com.foxminded.university.model.Address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ua.com.foxminded.university.dao.HibernateAddressDaoTest.TestData.*;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class HibernateAddressDaoTest {

    @Autowired
    private HibernateAddressDao addressDao;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    void givenNewAddress_onCreate_shouldCreateAddress() {
        var actual = hibernateTemplate.get(Address.class, 7);
        assertNull(actual);

        addressDao.create(addressToCreate);

        actual = hibernateTemplate.get(Address.class, 7);
        assertEquals(addressToCreate, actual);
    }

    @Test
    void givenCorrectAddressId_onFindById_shouldReturnOptionalWithCorrectAddress() {
        var expected = Optional.of(expectedAddress2);

        var actual = addressDao.findById(2);

        assertEquals(expected, actual);
    }

    @Test
    void givenIncorrectAddressId_onFindById_shouldReturnEmptyOptional() {
        Optional<Address> expected = Optional.empty();

        var actual = addressDao.findById(50);

        assertEquals(expected, actual);
    }

    @Test
    void ifDatabaseHasAddresss_onFindAll_shouldReturnCorrectListOfAddresss() {
        assertEquals(expectedAddresses, addressDao.findAll());
    }

    @Test
    void ifDatabaseHasNoAddresss_onFindAll_shouldReturnEmptyListOfAddresss() {
        hibernateTemplate.deleteAll(expectedAddresses);

        var addresss = addressDao.findAll();

        assertThat(addresss).isEmpty();
    }

    @Test
    void givenAddress_onUpdate_shouldUpdateCorrectly() {
        addressDao.update(addressToUpdate);

        var expected = hibernateTemplate.get(Address.class, 2);

        assertEquals(addressToUpdate, expected);
    }

    @Test
    void givenCorrectAddressId_onDelete_shouldDeleteCorrectly() {
        addressDao.delete(expectedAddress2);

        var expected = hibernateTemplate.get(Address.class, 2);
        assertNull(expected);
    }

    @Test
    void ifDatabaseHasAddresses_onFindAll_shouldReturnCorrectListOfAddresses() {
        List<Address> actual = addressDao.findAll();

        assertEquals(expectedAddresses, actual);
    }

    public interface TestData {
        Address addressToUpdate = Address.builder().country("test").id(2).postalCode("test").region("test")
                .city("test").streetAddress("test").build();

        Address addressToCreate = Address.builder().country("test").id(7).postalCode("test").region("test").city("test")
                .streetAddress("test").build();

        Address expectedAddress1 = Address.builder().country("UK").id(1).postalCode("12345").region("City-Of-Edinburgh")
                .city("Edinburgh").streetAddress("Panmure House").build();
        Address expectedAddress2 = Address.builder().country("Poland").id(2).postalCode("54321").region(
                        "Central region")
                .city("Warsaw").streetAddress("Urszuli Ledochowskiej 3").build();
        Address expectedAddress3 = Address.builder().country("Russia").id(3).postalCode("450080").region(
                        "Permskiy kray")
                .city("Perm").streetAddress("Lenina 5").build();
        Address expectedAddress4 = Address.builder().country("USA").id(4).postalCode("90210").region("California")
                .city("LA").streetAddress("Grove St. 15").build();
        Address expectedAddress5 = Address.builder().country("France").id(5).postalCode("21012").region("Central")
                .city("Paris").streetAddress("Rue 15").build();
        Address expectedAddress6 = Address.builder().country("China").id(6).postalCode("20121").region("Guangdung")
                .city("Beijin").streetAddress("Main St. 125").build();

        List<Address> expectedAddresses = new ArrayList<>(
                Arrays.asList(expectedAddress1, expectedAddress2, expectedAddress3,
                              expectedAddress4, expectedAddress5, expectedAddress6));
    }
}