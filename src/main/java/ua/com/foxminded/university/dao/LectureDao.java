package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LectureDao extends GeneralDao<Lecture> {

    List<Lecture> findByClassroom(Classroom classroom);

    List<Lecture> findByTeacher(Teacher teacher);

    Optional<Lecture> findByDateTimeClassroom(LocalDate date, Timeslot timeslot, Classroom classroom);

    List<Lecture> findByDateTime(LocalDate date, Timeslot timeslot);

    Optional<Lecture> findByDateTimeTeacher(LocalDate date, Timeslot timeslot, Teacher teacher);

    List<Lecture> findBySubject(Subject subject);

    List<Lecture> findByTimeslot(Timeslot timeslot);

    List<Lecture> findByTeacherAndPeriod(Teacher teacher, LocalDate start, LocalDate end);

    List<Lecture> findByStudentAndPeriod(Student student, LocalDate start, LocalDate end);
}
