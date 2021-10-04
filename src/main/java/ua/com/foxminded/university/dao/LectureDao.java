package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LectureDao extends GeneralDao<Lecture> {

    List<Lecture> findAll();

    List<Lecture> findByClassroom(Classroom classroom);

    List<Lecture> findByTeacher(Teacher teacher);

    Optional<Lecture> findByDateTimeClassroom(LocalDate date, Timeslot timeslot, Classroom classroom);

    List<Lecture> findByDateTime(LocalDate date, Timeslot timeslot);

    Optional<Lecture> findByDateTimeTeacher(LocalDate date, Timeslot timeslot, Teacher teacher);

    List<Lecture> findBySubject(Subject subject);

    List<Lecture> findByTimeslot(Timeslot timeslot);

    List<Lecture> findByTeacherAndDay(Teacher teacher, LocalDate date);

    List<Lecture> findByTeacherAndMonth(Teacher teacher, LocalDate date);

    List<Lecture> findByStudentAndDay(Student student, LocalDate date);

    List<Lecture> findByStudentAndMonth(Student student, LocalDate date);
}
