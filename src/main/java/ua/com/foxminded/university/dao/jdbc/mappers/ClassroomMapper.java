package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.jdbc.JdbcLocationDao;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Location;

@Component
public class ClassroomMapper implements RowMapper<Classroom> {

    private JdbcLocationDao jdbcLocationDAO;

    public ClassroomMapper(JdbcLocationDao jdbcLocationDao) {
	this.jdbcLocationDAO = jdbcLocationDao;
    }

    @Override
    public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {
	Classroom classroom = new Classroom();
	Location location = jdbcLocationDAO.findById(rs.getInt("location_id")).orElse(null);
	classroom.setId(rs.getInt("id"));
	classroom.setLocation(location);
	classroom.setName(rs.getString("name"));
	classroom.setCapacity(rs.getInt("capacity"));
	return classroom;
    }
}
