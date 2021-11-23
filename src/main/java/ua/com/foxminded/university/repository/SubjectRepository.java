package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.university.model.Subject;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    Optional<Subject> findByName(String name);

    @Query(value = "SELECT count(*) FROM teachers_subjects WHERE subject_id=:id", nativeQuery = true)
    long countAssignments(@Param("id")int id);
}
