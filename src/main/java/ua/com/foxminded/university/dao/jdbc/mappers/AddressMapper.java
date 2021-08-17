package ua.com.foxminded.university.dao.jdbc.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Address;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AddressMapper implements RowMapper<Address> {

    @Override
    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
        var address = new Address();
        address.setId(rs.getInt("id"));
        address.setCountry(rs.getString("country"));
        address.setPostalCode(rs.getString("postalCode"));
        address.setRegion(rs.getString("region"));
        address.setCity(rs.getString("city"));
        address.setStreetAddress(rs.getString("streetAddress"));

        return address;
    }
}
