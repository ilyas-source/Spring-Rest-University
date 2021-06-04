package ua.com.foxminded.university.dao.jdbc;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.LectureDAO;
import ua.com.foxminded.university.dao.jdbc.mappers.LectureMapper;
import ua.com.foxminded.university.model.Lecture;

@Component
public class JdbcLectureDAO implements LectureDAO {

    private static final String CREATE_ = "INSERT INTO lectures (name, description) VALUES (?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM lectures WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM lectures";
    private static final String UPDATE_ = "UPDATE lectures SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM lectures WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private LectureMapper lectureMapper;

    @Override
    public void addToDb(Lecture e) {
	// TODO Auto-generated method stub

    }

    @Override
    public Optional<Lecture> findById(int id) {
	return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, lectureMapper, id));
    }

    @Override
    public List<Lecture> findAll() {
	return jdbcTemplate.query(FIND_ALL, lectureMapper);
    }

    @Override
    public void update(Lecture e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void delete(int id) {
	jdbcTemplate.update(DELETE_BY_ID, id);
    }

}
