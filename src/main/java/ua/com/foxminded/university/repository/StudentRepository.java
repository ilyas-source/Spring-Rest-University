package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    int countByGroup(Group group);

    List<Student> findByGroup(Group group);

    Optional<Student> findByFirstNameAndLastNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);

    List<Student> findByFirstNameContainingOrLastNameContainingAllIgnoreCase(String firstName, String lastName);
}
