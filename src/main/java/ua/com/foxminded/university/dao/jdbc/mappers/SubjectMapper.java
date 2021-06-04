package ua.com.foxminded.university.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.model.Subject;

@Component
public class SubjectMapper implements RowMapper<Subject> {

    @Override
    public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {
	Subject subject = new Subject();
	subject.setId(rs.getInt("id"));
	subject.setName(rs.getString("name"));
	subject.setDescription(rs.getString("description"));
	return subject;
    }
}
