package ua.com.foxminded.university.dao.jdbc;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.HolidayDAO;
import ua.com.foxminded.university.model.Holiday;

@Component
public class JdbcHolidayDAO implements HolidayDAO {

    @Override
    public void create(Holiday e) {
	// TODO Auto-generated method stub

    }

    @Override
    public Optional<Holiday> findById(int id) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Holiday> findAll() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void update(Holiday e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void delete(int id) {
	// TODO Auto-generated method stub

    }

}
