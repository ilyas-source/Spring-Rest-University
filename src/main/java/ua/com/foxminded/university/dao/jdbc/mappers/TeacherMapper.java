package ua.com.foxminded.university.dao.jdbc.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.SubjectDao;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.Degree;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Teacher;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TeacherMapper implements RowMapper<Teacher> {

    private AddressDao addressDao;
    private SubjectDao subjectDao;
    private VacationDao vacationDao;

    public TeacherMapper(AddressDao addressDao, SubjectDao subjectDao, VacationDao vacationDao) {
        this.addressDao = addressDao;
        this.subjectDao = subjectDao;
        this.vacationDao = vacationDao;
    }

    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        var teacher = new Teacher();
        teacher.setId(rs.getInt("id"));
        teacher.setFirstName(rs.getString("first_name"));
        teacher.setLastName(rs.getString("last_name"));
        teacher.setEmail(rs.getString("email"));
        teacher.setPhoneNumber(rs.getString("phone"));
        teacher.setGender(Gender.valueOf(rs.getString("gender")));
        teacher.setDegree(Degree.valueOf(rs.getString("degree")));
        addressDao.findById(rs.getInt("address_id")).ifPresent(teacher::setAddress);
        teacher.setSubjects(subjectDao.getByTeacherId(teacher.getId()));
        teacher.setVacations(vacationDao.findByTeacherId(teacher.getId()));

        return teacher;
    }
}
