package ua.com.foxminded.university.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentDao extends GeneralDao<Student> {

    int countInGroup(Group group);

    Optional<Student> findByAddressId(int id);

    List<Student> findByGroup(Group group);

    Optional<Student> findByNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);

    Page<Student> findAll(Pageable pageable);
}
