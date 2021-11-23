package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    int countByGroup(Group group);

    List<Student> findByGroup(Group group);

    Optional<Student> findByFirstNameAndLastNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);

    @Query("FROM Student WHERE lower(concat(firstName,' ',lastName)) like concat('%', lower(:substring), '%')")
    List<Student> findBySubstring(@Param("substring") String substring);
}
