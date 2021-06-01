package ua.com.foxminded.university.model.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ua.com.foxminded.university.model.Subject;

public class SubjectMapper implements RowMapper<Subject> {

    @Override
    public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {
	Subject subject = new Subject();
	subject.setName(rs.getString("name"));
	subject.setDescription(rs.getString("description"));

	return subject;
    }

}
