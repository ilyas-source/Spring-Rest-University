package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {

    List<Lecture> findByClassroom(Classroom classroom);

    List<Lecture> findByTeacher(Teacher teacher);

    Optional<Lecture> findByDateAndTimeslotAndClassroom(LocalDate date, Timeslot timeslot, Classroom classroom);

    List<Lecture> findByDateAndTimeslot(LocalDate date, Timeslot timeslot);

    Optional<Lecture> findByDateAndTimeslotAndTeacher(LocalDate date, Timeslot timeslot, Teacher teacher);

    List<Lecture> findBySubject(Subject subject);

    List<Lecture> findByTimeslot(Timeslot timeslot);

    List<Lecture> findByTeacherAndDateBetween(Teacher teacher, LocalDate start, LocalDate end);

  //  List<Lecture> findByStudentAndDateBetween(Student student, LocalDate start, LocalDate end);
}
