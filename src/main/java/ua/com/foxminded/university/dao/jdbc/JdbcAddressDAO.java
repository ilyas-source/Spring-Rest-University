package ua.com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.AddressDAO;
import ua.com.foxminded.university.dao.jdbc.mappers.AddressMapper;
import ua.com.foxminded.university.model.Address;

@Component
public class JdbcAddressDAO implements AddressDAO {

    private static final String CREATE = "INSERT INTO addresses (country, postalcode, region, city, streetAddress) VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM addresses WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM addresses";
    private static final String UPDATE = "UPDATE addresses SET country = ?, postalcode = ?, region = ?, city = ?, streetAddress = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM addresses WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AddressMapper addressMapper;

    public void addToDb(Address address) {
	KeyHolder keyHolder = new GeneratedKeyHolder();

	jdbcTemplate.update(connection -> {
	    PreparedStatement ps = connection
		    .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
	    ps.setString(1, address.getCountry());
	    ps.setString(2, address.getPostalCode());
	    ps.setString(3, address.getRegion());
	    ps.setString(4, address.getCity());
	    ps.setString(5, address.getStreetAddress());
	    return ps;
	}, keyHolder);
	address.setId((int) keyHolder.getKeys().get("id"));
    }

    @Override
    public Optional<Address> findById(int id) {
	return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, addressMapper, id));
    }

    @Override
    public List<Address> findAll() {
	return jdbcTemplate.query(FIND_ALL, addressMapper);
    }

    @Override
    public void update(Address address) {
	jdbcTemplate.update(UPDATE, address.getCountry(), address.getPostalCode(), address.getRegion(), address.getCity(),
		address.getStreetAddress(),
		address.getId());
    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }

}
