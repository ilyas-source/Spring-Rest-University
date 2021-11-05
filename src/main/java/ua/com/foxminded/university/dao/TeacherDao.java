package ua.com.foxminded.university.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.model.Lecture;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherDao extends GeneralDao<Teacher> {

    Page<Teacher> findAll(Pageable pageable);

    Optional<Teacher> findByNameAndEmail(String firstName, String lastName, String email);

    List<Teacher> findBySubstring(String substring);

    List<Teacher> getReplacementCandidates(Lecture lecture);
}
