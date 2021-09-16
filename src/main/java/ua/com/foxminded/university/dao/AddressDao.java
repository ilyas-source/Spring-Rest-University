package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Address;

import java.util.List;

public interface AddressDao extends GeneralDao<Address> {

    List<Address> findAll();
}
