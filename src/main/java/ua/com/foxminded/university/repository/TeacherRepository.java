package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Optional<Teacher> findByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email);

    @Query("FROM Teacher WHERE lower(concat(firstName,' ',lastName)) like concat('%', lower(:substring), '%')")
    List<Teacher> findBySubstring(@Param("substring") String substring);

    @Query(value="SELECT * FROM teachers JOIN teachers_subjects ts " +
                "ON teachers.id = ts.teacher_id WHERE subject_id= :subject_id AND teacher_id!= :teacher_id",
                nativeQuery = true)
    List<Teacher> getReplacementCandidates(@Param("subject_id") int subjectId,
                                           @Param("teacher_id") int teacherId);

}