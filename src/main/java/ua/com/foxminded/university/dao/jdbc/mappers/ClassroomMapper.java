package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.LocationDao;
import ua.com.foxminded.university.model.Classroom;

@Component
public class ClassroomMapper implements RowMapper<Classroom> {

    private LocationDao locationDao;

    public ClassroomMapper(LocationDao locationDao) {
	this.locationDao = locationDao;
    }

    @Override
    public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {
	Classroom classroom = new Classroom();
	classroom.setId(rs.getInt("id"));
	locationDao.findById(rs.getInt("location_id")).ifPresent(classroom::setLocation);
	classroom.setName(rs.getString("name"));
	classroom.setCapacity(rs.getInt("capacity"));
	return classroom;
    }
}
