package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Vacation;

import java.util.List;

public interface VacationRepository extends JpaRepository<Vacation, Integer> {

    List<Vacation> findByTeacher(Teacher teacher);
}
