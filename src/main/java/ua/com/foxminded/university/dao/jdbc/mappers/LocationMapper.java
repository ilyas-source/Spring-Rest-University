package ua.com.foxminded.university.dao.jdbc.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Location;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LocationMapper implements RowMapper<Location> {

    @Override
    public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
        var location = new Location();
        location.setId(rs.getInt("id"));
        location.setBuilding(rs.getString("building"));
        location.setFloor(rs.getInt("floor"));
        location.setRoomNumber(rs.getInt("room_number"));

        return location;
    }
}
