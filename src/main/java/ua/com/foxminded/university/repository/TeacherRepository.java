package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Optional<Teacher> findByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email);

    List<Teacher> findByFirstNameContainingOrLastNameContainingAllIgnoreCase(String firstName, String lastName);

    List<Teacher> findBySubjects(Subject subject);
}
